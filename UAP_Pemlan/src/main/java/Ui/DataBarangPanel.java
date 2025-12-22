package Ui;
import Model.Barang;
import Service.InventoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
public class DataBarangPanel extends  JPanel {
    private final InventoryService service;

    public Runnable onAdd;
    public java.util.function.Consumer<String> onEdit;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Kode","Nama","Kategori","Stok","Harga"}, 0
    ) { public boolean isCellEditable(int r, int c){ return false; } };

    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final JTextField tfSearch = new JTextField();

    public DataBarangPanel(InventoryService service) {
        this.service = service;

        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Data Barang");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        table.setRowSorter(sorter);

        JPanel top = new JPanel(new BorderLayout(8,8));
        top.add(title, BorderLayout.NORTH);

        JPanel search = new JPanel(new BorderLayout(6,6));
        search.add(new JLabel("Search (kode/nama/kategori): "), BorderLayout.WEST);
        search.add(tfSearch, BorderLayout.CENTER);
        JButton btnCari = new JButton("Cari");
        JButton btnReset = new JButton("Reset");
        JPanel sb = new JPanel(new GridLayout(1,2,6,6));
        sb.add(btnCari); sb.add(btnReset);
        search.add(sb, BorderLayout.EAST);
        top.add(search, BorderLayout.SOUTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        JButton btnSortStok = new JButton("Sort Stok");
        JButton btnSortHarga = new JButton("Sort Harga");
        JButton btnSortNama = new JButton("Sort Nama");

        actions.add(btnTambah); actions.add(btnEdit); actions.add(btnHapus);
        actions.add(btnSortStok); actions.add(btnSortHarga); actions.add(btnSortNama);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        btnCari.addActionListener(e -> applyFilter());
        btnReset.addActionListener(e -> { tfSearch.setText(""); sorter.setRowFilter(null); });

        btnTambah.addActionListener(e -> { if (onAdd != null) onAdd.run(); });
        btnEdit.addActionListener(e -> {
            String kode = selectedKode();
            if (kode != null && onEdit != null) onEdit.accept(kode);
        });

        btnHapus.addActionListener(e -> deleteSelected());

        // Sorting pakai Comparator (di service)
        btnSortStok.addActionListener(e -> refreshTable(service.sortByStokAsc()));
        btnSortHarga.addActionListener(e -> refreshTable(service.sortByHargaAsc()));
        btnSortNama.addActionListener(e -> refreshTable(service.sortByNamaAsc()));

        refreshTable(service.getAll());
    }
    private void applyFilter() {
        String key = tfSearch.getText().trim();
        if (key.isEmpty()) { sorter.setRowFilter(null); return; }

        // Escape input agar dianggap teks biasa, bukan regex
        String safe = java.util.regex.Pattern.quote(key);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + safe));
    }
    private String selectedKode() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih data dulu."); return null; }
        int mRow = table.convertRowIndexToModel(row);
        return model.getValueAt(mRow, 0).toString();
    }
    private void deleteSelected() {
        String kode = selectedKode();
        if (kode == null) return;

        int ok = JOptionPane.showConfirmDialog(this, "Hapus barang " + kode + " ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        try { // try-catch wajib
            service.delete(kode);
            refreshTable(service.getAll());
            JOptionPane.showMessageDialog(this, "Berhasil dihapus.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal hapus: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refreshTable(List<Barang> data) {
        model.setRowCount(0);
        for (Barang b : data) model.addRow(new Object[]{b.kode, b.nama, b.kategori, b.stok, b.harga});
    }
}