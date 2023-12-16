package com.necibeguner.urunekleme

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.necibeguner.urunekleme.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.UUID

class MainActivity : AppCompatActivity() {

    // Ana aktivite layout'unun bağlama (binding) örneği
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // Seçilen resimleri ve renkleri tutmak için listeler
    private var selectedImages = mutableListOf<Uri>()
    private val selectedColors = mutableListOf<Int>()

    // Firebase depolama ve Firestore referansları
    private val productStorage = Firebase.storage.reference
    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // İçeriği şişirilmiş düzenlemeyle eşleştirme

        // Renk seçici ile ilgili işlevselliği ayarlama
        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Ürün Rengi")
                .setPositiveButton("Seçiniz", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {
                            selectedColors.add(it.color) // Seçilen renkleri listeye ekleme
                            updateColors() // Seçilen renkleri göstermek için arayüzü güncelleme
                        }
                    }
                })
                .setNegativeButton("İptal") { colorPicker, _ ->
                    colorPicker.dismiss()
                }.show()
        }

        // Resim seçme işlevselliğiyle ilgili işlem
        val selectedImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data

                    // Birden fazla resmin seçilmesini ele alma
                    if (intent?.clipData != null) {
                        val count = intent.clipData?.itemCount ?: 0
                        (0 until count).forEach {
                            val imageUri = intent.clipData?.getItemAt(it)?.uri
                            imageUri?.let {
                                selectedImages.add(it) // Seçilen resimleri listeye ekleme
                            }
                        }
                    } else {
                        val imageUri = intent?.data
                        imageUri?.let { selectedImages.add(it) } // Tek bir seçilen resmi listeye ekleme
                    }
                    updateImages() // Seçilen resimlerin sayısını göstermek için arayüzü güncelleme
                }
            }

        // Resim seçme işlemini tetikleme
        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectedImagesActivityResult.launch(intent)
        }
    }

    // Arayüzü seçilen resimlerin sayısını göstermek için güncelleme
    private fun updateImages() {
        binding.tvSelectedImages.text = selectedImages.size.toString()
    }

    // Seçilen renkleri göstermek için arayüzü güncelleme
    private fun updateColors() {
        var colors = ""
        selectedColors.forEach {
            colors = "$colors ${Integer.toHexString(it)}"
        }
        binding.tvSelectedColors.text = colors
    }

    // Toolbar içindeki seçenek menüsü oluşturma
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // Toolbar menü öğesi seçimini ele alma
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveProduct) {
            val productValidation = validateInformation()
            if (!productValidation) {
                Toast.makeText(this, "Veri Girişleriniz Kontrol Edin", Toast.LENGTH_SHORT).show()
                return false
            }

            saveProduct() // Ürün kaydetme işlemini başlatma
        }
        return super.onOptionsItemSelected(item)
    }

    // Ürün detaylarını Firebase Firestore'a kaydetme
    private fun saveProduct() {
        // Kullanıcı arayüzünden girdi değerlerini alın
        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val offerPercentage = binding.edofferPercentage.text.toString().trim()
        val description = binding.edDescription.text.toString().trim()
        val size = getSizeList(binding.edSizes.text.toString().trim())
        val imagesByteArrays = getImagesByteArrays()
        val images = mutableListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading() // Ürün kaydedilirken yüklenme durumunu gösterme
            }

            try {
                // Seçilen resimleri Firebase Storage a yükleme
                async {
                    imagesByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val imageStorage = productStorage.child("products/images/$id")
                            val result = imageStorage.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl) // Resimlerin indirme URL'lerini saklama
                        }
                    }
                }.await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoading() // Hata durumunda yüklenme durumunu gizleme
                }
                e.printStackTrace()
            }

            // Tüm detayları içeren bir product nesnesi oluşturma
            val product = Product(
                UUID.randomUUID().toString(),
                name,
                category,
                price.toFloat(),
                if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                if (description.isEmpty()) null else description,
                if (selectedColors.isEmpty()) null else selectedColors,
                size,
                images
            )

            // Firestore'a ürün ekleme
            firestore.collection("Products").add(product)
                .addOnSuccessListener {
                    hideLoading() // Ürün başarıyla eklendikten sonra yüklenme durumunu gizleme
                }.addOnFailureListener {
                    hideLoading() // Başarısızlık durumunda yüklenme durumunu gizleme
                    Log.e("Hata", it.message.toString())
                }
        }
    }

    // Yüklenme durumunu gizleme
    private fun hideLoading() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    // Yüklenme durumunu gösterme
    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }

    // Depolama için seçilen resimleri byte dizilerine dönüştürme
    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imageBmp = MediaStore.Images.Media.getBitmap(contentResolver, it)
            if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                imagesByteArray.add(stream.toByteArray())
            }
        }
        return imagesByteArray
    }

    // Boyut girişini bir liste olarak ayrıştırma
    private fun getSizeList(sizeStr: String): List<String>? {
        if (sizeStr.isEmpty())
            return null
        val sizeList = sizeStr.split(",")
        return sizeList
    }

    // Ürünü kaydetmeden önce gerekli bilgileri doğrulama
    private fun validateInformation(): Boolean {
        if (binding.edPrice.text.toString().trim().isEmpty())
            return false
        if (binding.edName.text.toString().trim().isEmpty())
            return false
        if (binding.edCategory.text.toString().trim().isEmpty())
            return false
        if (selectedImages.isEmpty())
            return false
        return true
    }
}
