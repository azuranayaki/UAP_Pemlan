package Service;
import Model.Barang;
import Util.Storage;

import java.io.IOException;
import java.util.*;

public class InventoryService {
    private final ArrayList<Barang> cache = new ArrayList<>();
    public InventoryService() {
        try { cache.addAll(Storage.loadBarang()); }
        catch (Exception ignored) {}
    }
    public List<Barang> getAll() { return new ArrayList<>(cache); }

    public Optional<Barang> findByKode(String kode) {
        return cache.stream().filter(b -> b.kode.equalsIgnoreCase(kode)).findFirst();
    }
    public void add(Barang b) throws IOException {
        validate(b, true);
        cache.add(b);
        Storage.saveBarang(cache);
        Storage.appendHistory("ADD " + b.kode + " " + b.nama);
    }
    public void update(String kode, Barang newData) throws IOException {
        // kode primary key tidak boleh berubah
        if (newData.kode != null && !newData.kode.equalsIgnoreCase(kode)) {
            throw new IllegalArgumentException("Kode barang tidak boleh diubah.");
        }

        validate(newData, false);
        Barang target = findByKode(kode).orElseThrow(() -> new IllegalArgumentException("Barang tidak ditemukan."));

        int stokLama = target.stok, hargaLama = target.harga;

        target.nama = newData.nama;
        target.kategori = newData.kategori;
        target.stok = newData.stok;
        target.harga = newData.harga;

        Storage.saveBarang(cache);
        Storage.appendHistory("UPDATE " + target.kode + " stok " + stokLama + "->" + target.stok +
                " harga " + hargaLama + "->" + target.harga);
    }
    public void delete(String kode) throws IOException {
        Barang target = findByKode(kode).orElseThrow(() -> new IllegalArgumentException("Barang tidak ditemukan."));
        cache.removeIf(b -> b.kode.equalsIgnoreCase(kode));
        Storage.saveBarang(cache);
        Storage.appendHistory("DELETE " + target.kode + " " + target.nama);
    }
    // Sorting pakai Comparator (poin instruksi)
    public List<Barang> sortByStokAsc() {
        List<Barang> list = getAll();
        list.sort(Comparator.comparingInt(b -> b.stok));
        return list;
    }
    public List<Barang> sortByHargaAsc() {
        List<Barang> list = getAll();
        list.sort(Comparator.comparingInt(b -> b.harga));
        return list;
    }
    public List<Barang> sortByNamaAsc() {
        List<Barang> list = getAll();
        list.sort(Comparator.comparing(b -> (b.nama == null ? "" : b.nama.toLowerCase())));
        return list;
    }
    public int totalStok() {
        int sum = 0;
        for (Barang b : cache) sum += b.stok;
        return sum;
    }
    public long totalNilai() {
        long sum = 0;
        for (Barang b : cache) sum += (long) b.stok * b.harga;
        return sum;
    }
    public int hampirHabisCount(int threshold) {
        int c = 0;
        for (Barang b : cache) if (b.stok <= threshold) c++;
        return c;
    }
    private void validate(Barang b, boolean isCreate) {
        requireNotBlank(b.kode, "Kode");
        requireNotBlank(b.nama, "Nama");
        requireNotBlank(b.kategori, "Kategori");
        if (b.stok < 0) throw new IllegalArgumentException("Stok tidak boleh negatif.");
        if (b.harga < 0) throw new IllegalArgumentException("Harga tidak boleh negatif.");
        if (isCreate && findByKode(b.kode).isPresent()) throw new IllegalArgumentException("Kode barang sudah ada.");
    }
    private void requireNotBlank(String s, String field) {
        if (s == null || s.trim().isEmpty()) throw new IllegalArgumentException(field + " tidak boleh kosong.");
    }
}