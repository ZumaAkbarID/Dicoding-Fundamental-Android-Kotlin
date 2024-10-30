# BACA INI!
GUNAKAN SEBAGAI REFERENSI JANGAN PLEK KETIPLEK, DARI PROJECT INI, DICODING BISA TAWU. eh gatau deng:v

**use at your own risk!**

# Kriteria
Fitur yang harus ditambahkan pada aplikasi:

Membuat Fitur Favorite dengan Database

Aplikasi harus bisa menambah dan menghapus event favorite.

Aplikasi harus mempunyai halaman yang menampilkan daftar favorite.

Menampilkan halaman detail dari daftar favorite.

Membuat Pengaturan Tema

Membuat menu untuk mengganti tema (light theme atau dark theme) dengan menggunakan penyimpanan key-value.

Pastikan tema tetap terimplementasi walaupun aplikasi ditutup dan dibuka kembali. 

Caranya yaitu dengan meng-observe data dan mengimplementasikan tema pada halaman pertama.

Pastikan setiap komponen dan indikator tetap terlihat jelas ketika berubah tema

Ketika menggunakan tema gelap, teks dan indikator jangan berwarna hitam.

Mempertahankan semua fitur aplikasi dan komponen yang digunakan pada Submission Awal.

Aplikasi tetap menampilkan minimal 2 Jenis List Event dengan Bottom Navigation.

Aplikasi tetap menampilkan halaman Detail Event.

Aplikasi tetap menampilkan indikator Loading.

# Kriteria Opsional
Membuat Fitur Pengaturan Daily Reminder

Menambahkan menu pengaturan daily reminder untuk event aktif terdekat.

Anda bisa menggunakan endpoint untuk menampilkan event aktif kemudian mengambil satu data pertama, atau

Menggunakan parameter limit pada endpoint. Misalnya:
https://event-api.dicoding.dev/events?active=-1&limit=1

Menggunakan WorkManager untuk membuat Periodic Work dengan interval 1 hari.

Notifikasi berisi data berupa nama dan waktu event terdekat.

Pastikan notifikasi hanya tampil ketika menu pengaturan notifikasi dalam keadaan aktif.

Mengimplementasikan Repository dan Injection

Menerapkan Repository dan Injection dengan benar untuk data yang diambil dari API. 

Mengimplementasikan Coroutine

Menggunakan Coroutine untuk implementasi Retrofit dan Room.

Mengimplementasikan Error Handling

Aplikasi bisa memberikan pesan error jika data tidak berhasil ditampilkan. Misalnya, ketika tidak ada internet atau gagal mendapatkan data dari API.

Meningkatkan Kualitas Kode

Warning di Inspect Code (Code → Inspect Code) tidak lebih dari 10 (typo tidak dihitung).
