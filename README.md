# Inventaris Toko (Java Swing)
Aplikasi desktop sederhana untuk mengelola inventaris barang toko menggunakan **Java Swing**.  
Fitur utama: **CRUD**, **search**, **sorting (Comparator)**, **laporan & history**, serta **persistensi data** menggunakan **File Handling** (CSV + TXT) sehingga data tidak hilang saat aplikasi ditutup.
---
## Fitur Utama
### 1) GUI (Java Swing) - 4 Screen
1. **Dashboard**
    - Menampilkan ringkasan: total jenis barang & total stok.
2. **Data Barang (List Data)**
    - Menampilkan tabel data barang (JTable).
    - Fitur **search** berdasarkan kode/nama/kategori.
    - Fitur **sorting** berdasarkan stok/harga/nama.
    - Tombol **Tambah**, **Edit**, **Hapus**.
3. **Form Barang (Input)**
    - Form tambah/edit data barang (kode, nama, kategori, stok, harga).
    - Validasi input menggunakan **try-catch**.
4. **Laporan/History**
    - Ringkasan: total barang, total stok, total nilai stok, barang hampir habis (stok <= 3).
    - Menampilkan **riwayat aktivitas** CRUD dari file history.