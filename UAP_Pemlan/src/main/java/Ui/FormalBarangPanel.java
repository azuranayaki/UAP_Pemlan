package Ui;
import Model.Barang;
import Service.InventoryService;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
public class FormalBarangPanel extends  JPanel {
    private final InventoryService service;

    public Runnable onSaved;
    public Runnable onCancel;

    private boolean editMode = false;
    private String editKode = null;

    private final JLabel lblTitle = new JLabel("Form Barang");
    private final JTextField tfKode = new JTextField();
    private final JTextField tfNama = new JTextField();
    private final JComboBox<String> cbKategori = new JComboBox<>(new String[]{
            "Sembako","Minuman","Snack","RumahTangga","Lainnya"
    });
    private final JTextField tfStok = new JTextField();
    private final JTextField tfHarga = new JTextField();

    public FormalBarangPanel(InventoryService service) {
        this.service = service;

        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));

        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        form.add(new JLabel("Kode Barang")); form.add(tfKode);
        form.add(new JLabel("Nama Barang")); form.add(tfNama);
        form.add(new JLabel("Kategori")); form.add(cbKategori);
        form.add(new JLabel("Stok (angka)")); form.add(tfStok);
        form.add(new JLabel("Harga (angka)")); form.add(tfHarga);

        JButton btnSave = new JButton("Simpan");
        JButton btnReset = new JButton("Reset");
        JButton btnBack = new JButton("Kembali");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        actions.add(btnBack); actions.add(btnReset); actions.add(btnSave);

        add(lblTitle, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        btnReset.addActionListener(e -> resetFields());
        btnBack.addActionListener(e -> { if (onCancel != null) onCancel.run(); });
        btnSave.addActionListener(e -> onSave());
    }

    public void openCreate() {
        editMode = false;
        editKode = null;
        lblTitle.setText("Tambah Barang");
        tfKode.setEnabled(true);
        resetFields();
    }

    public void openEdit(String kode) {
        editMode = true;
        editKode = kode;
        lblTitle.setText("Edit Barang (" + kode + ")");
        tfKode.setEnabled(false);

        Barang b = service.findByKode(kode).orElse(null);
        if (b == null) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            openCreate();
            return;
        }
        tfKode.setText(b.kode);
        tfNama.setText(b.nama);
        cbKategori.setSelectedItem(b.kategori);
        tfStok.setText(String.valueOf(b.stok));
        tfHarga.setText(String.valueOf(b.harga));
    }

    private void resetFields() {
        tfKode.setText("");
        tfNama.setText("");
        cbKategori.setSelectedIndex(0);
        tfStok.setText("");
        tfHarga.setText("");
    }

    private void onSave() {
        try {
            String kode = tfKode.getText().trim();
            String nama = tfNama.getText().trim();
            String kategori = Objects.toString(cbKategori.getSelectedItem(), "").trim();

            if (kode.isEmpty()) throw new IllegalArgumentException("Kode wajib diisi.");
            if (nama.isEmpty()) throw new IllegalArgumentException("Nama wajib diisi.");
            if (kategori.isEmpty()) throw new IllegalArgumentException("Kategori wajib dipilih.");
            if (tfStok.getText().trim().isEmpty()) throw new IllegalArgumentException("Stok wajib diisi.");
            if (tfHarga.getText().trim().isEmpty()) throw new IllegalArgumentException("Harga wajib diisi.");

            int stok = Integer.parseInt(tfStok.getText().trim());
            int harga = Integer.parseInt(tfHarga.getText().trim());

            Barang b = new Barang(kode, nama, kategori, stok, harga);

            if (!editMode) service.add(b);
            else service.update(editKode, b);

            JOptionPane.showMessageDialog(this, "Berhasil disimpan.");
            if (onSaved != null) onSaved.run();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stok & Harga harus angka.", "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
