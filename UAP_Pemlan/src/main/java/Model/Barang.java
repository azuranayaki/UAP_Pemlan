package Model;
public class Barang {
    public String kode, nama, kategori;
    public int stok, harga;

    public Barang(String kode, String nama, String kategori, int stok, int harga) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.stok = stok;
        this.harga = harga;
    }
    public String toCsv() {
        return safe(kode) + "," + safe(nama) + "," + safe(kategori) + "," + stok + "," + harga;
    }
    public static Barang fromCsv(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 5) throw new IllegalArgumentException("Format CSV tidak valid: " + line);

        try {
            int stok = Integer.parseInt(p[3].trim());
            int harga = Integer.parseInt(p[4].trim());
            return new Barang(p[0], p[1], p[2], stok, harga);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Stok/Harga harus angka pada baris CSV: " + line);
        }
    }
    private static String safe(String s) {
        return (s == null ? "" : s.replace(",", " "));
    }
}