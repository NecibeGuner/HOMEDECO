# HOMEDECO Mobil Uygulaması

Bu depo, Kocaeli Üniversitesi Bilişim Sistemleri Mühendisliği öğrencileri tarafından geliştirilen **HOMEDECO Mobilya Satış Uygulaması**nın Android tabanlı yönetici (admin) paneli kodlarını içermektedir. Uygulama geliştirme sürecinde **Kotlin**, **Android Studio** ve **Firebase** teknolojileri kullanılmıştır.

## Yazar

* **Necibe GÜNER** – [221307049@kocaeli.edu.tr](mailto:221307049@kocaeli.edu.tr)

## Proje Hakkında

HOMEDECO Mobil Uygulaması, yöneticilerin mobil cihaz üzerinden ürün ve kategori yönetimini, sipariş takibini ve kullanıcı yetkilendirmesini gerçekleştirebilecekleri bir Android uygulamasıdır. Firebase altyapısı ile gerçek zamanlı veri akışı, kimlik doğrulama ve medya dosya yönetimi sağlanmıştır.

### Temel Özellikler

* Ürün ve kategori ekleme, düzenleme ve silme
* Gelen siparişlerin listelenmesi ve durum güncellemeleri
* Kullanıcı hesapları için yetkilendirme ve profil yönetimi
* Firebase Firestore üzerinden gerçek zamanlı veri senkronizasyonu
* Firebase Authentication ile güvenli kullanıcı girişi
* Firebase Cloud Storage ile ürün görsellerinin depolanması

## Teknoloji Yığını

* Kotlin (Android uygulama dili)
* Android Studio (IDE)
* Firebase Auth (kimlik doğrulama)
* Firebase Firestore (veritabanı)
* Firebase Cloud Storage (medya dosyaları)
* Google Play Services
* Material Components for Android

## Kurulum

1. Depoyu klonlayın:

   ```bash
   git clone https://github.com/NecibeGuner/HOMEDECO.git
   cd HOMEDECO
   ```
2. Android Studio ile projeyi açın:

   * **Open an existing Android Studio project** seçeneği ile proje klasörünü seçin.
3. Firebase yapılandırma dosyasını ekleyin:

   * Firebase Console üzerinden Android uygulama ekleyin.
   * İndirdiğiniz `google-services.json` dosyasını `app/` klasörüne yerleştirin.
4. Gradle projeyi senkronize edin:

   * Android Studio içinde **Sync Project with Gradle Files** butonuna tıklayın.
5. Uygulamayı çalıştırın:

   * Emulator veya USB ile bağlı cihazı seçip **Run** butonuna basın.

## Proje Yapısı

```
HOMEDECO/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/homedeco/admin/   # Kotlin kaynak kodları
│   │   │   ├── res/                        # XML layout, drawable, values
│   │   │   └── AndroidManifest.xml        # Uygulama tanımı
│   └── build.gradle                       # Modül yapılandırması
├── build.gradle                           # Proje yapılandırması
├── settings.gradle
├── google-services.json                   # Firebase yapılandırma dosyası
└── README.md                              # Proje dokümantasyonu
```

## Kullanım

* Uygulamayı açtığınızda Firebase Authentication ekranı ile giriş yapabilirsiniz.
* Ana ekranda ürün listesi ve siparişler sekmeleri bulunmaktadır.
* Yeni ürün ya da kategori eklemek için sağ alt köşedeki **+** butonuna tıklayın.
* Sipariş detaylarına girerek durum güncellemesi yapabilirsiniz.
* Ayarlar menüsünden kullanıcı profili ve çıkış işlemi gerçekleştirilebilir.

## Lisans

Bu proje akademik amaçlı geliştirilmiştir ve ticari bir lisansa tabi değildir.

## İletişim

Her türlü soru ve geri bildirim için lütfen e-posta yoluyla ulaşın:

* Necibe GÜNER – [221307049@kocaeli.edu.tr](mailto:221307049@kocaeli.edu.tr)
