# HOMEDECO Admin Panel

Bu depo, Kocaeli Üniversitesi Bilişim Sistemleri Mühendisliği öğrencileri tarafından geliştirilen **HOMEDECO Mobilya Satış Uygulaması**nın yönetici (admin) paneli kodlarını içermektedir.

## Yazar

* **Necibe GÜNER** – [221307049@kocaeli.edu.tr](mailto:221307049@kocaeli.edu.tr)

## Proje Hakkında

HOMEDECO, kullanıcılara çevrimiçi mobilya satışı yapma imkânı sunan bir e‑ticaret platformudur. Bu repoda, satış platformunun arka planda çalışacak **yönetici paneli** (admin dashboard) uygulaması bulunmaktadır. Admin paneli üzerinden:

* Ürün ve kategori yönetimi (oluşturma, güncelleme, silme)
* Sipariş listesi ve durumu takibi
* Kullanıcı hesaplarının görüntülenmesi ve yetkilendirme
* Site içerik (banner, kampanya duyuruları) yönetimi
* Raporlama ve istatistik panelleri

gibi temel işlevler gerçekleştirilebilir.

## Teknoloji Yığını

* Python 3.13
* Django 5.2
* PostgreSQL (veritabanı)
* HTML5, CSS3, Bootstrap 5
* JavaScript (jQuery)
* AWS S3 / Django Storages (opsiyonel, medya dosyaları için)

## Kurulum

1. Depoyu klonlayın:

   ```bash
   git clone https://github.com/NecibeGuner/HOMEDECO.git
   cd HOMEDECO
   ```
2. Sanal ortam oluşturun ve aktif edin:

   ```bash
   python -m venv venv
   source venv/bin/activate  # Windows: venv\Scripts\activate
   ```
3. Gereksinimleri yükleyin:

   ```bash
   pip install -r requirements.txt
   ```
4. Veritabanı ayarlarını yapılandırın:

   * `.env` dosyası oluşturun ve gerekli ortam değişkenlerini ekleyin (`SECRET_KEY`, `DEBUG`, `DATABASE_URL`, AWS bilgileri vb.).
5. Migration işlemlerini çalıştırın:

   ```bash
   python manage.py migrate
   ```
6. Yönetici kullanıcısı oluşturun:

   ```bash
   python manage.py createsuperuser
   ```
7. Geliştirme sunucusunu başlatın:

   ```bash
   python manage.py runserver
   ```

## Kullanım

Tarayıcınızda `http://127.0.0.1:8000/admin/` adresine giderek oluşturduğunuz yönetici hesabıyla giriş yapabilirsiniz. Admin paneli üzerinde ürün, sipariş ve kullanıcı kayıtlarını yönetebilirsiniz.

## Proje Yapısı

```
HOMEDECO/
├── home_deco/            # Django ana proje dizini (settings, urls)
├── apps/                 # Uygulama (app) modülleri
│   ├── products/         # Ürün yönetimi
│   ├── orders/           # Sipariş yönetimi
│   └── users/            # Kullanıcı yönetimi ve yetkilendirme
├── media/                # Yüklenen medya dosyaları
├── static/               # Statik dosyalar (CSS, JS, img)
├── templates/            # Şablon dosyaları
├── requirements.txt      # Python paketleri
└── README.md             # Proje dokümantasyonu
```

## Katkıda Bulunma

Akademik proje düzenlemeleri ve hatırlatmalar için Fork yapıp, branch oluşturduktan sonra Pull Request gönderebilirsiniz.

## Lisans

Bu proje, akademik amaçlı geliştirilmiştir ve ticari bir lisansa tabi değildir.

## İletişim

Her türlü soru ve geri bildirim için lütfen e-posta yoluyla ulaşın:

* Necibe GÜNER – [221307049@kocaeli.edu.tr](mailto:221307049@kocaeli.edu.tr)

