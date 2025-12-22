package Ui;
import Service.InventoryService;
import Util.Storage;

import javax.swing.*;
import java.awt.*;
public class LaporanPanel extends JPanel {
    private final InventoryService service;

    public Runnable onBackDashboard;

    private final JLabel lblTotalBarang = new JLabel("0");
    private final JLabel lblTotalStok   = new JLabel("0");
    private final JLabel lblTotalNilai  = new JLabel("0");
    private final JLabel lblHampirHabis = new JLabel("0");

    private final JTextArea taHistory = new JTextArea();

    public LaporanPanel(InventoryService service) {
        this.service = service;

        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Laporan & History");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JPanel summary = new JPanel(new GridLayout(1,4,10,10));
        summary.add(box("Total Barang", lblTotalBarang));
        summary.add(box("Total Stok", lblTotalStok));
        summary.add(box("Total Nilai", lblTotalNilai));
        summary.add(box("Hampir Habis (<=3)", lblHampirHabis));

        taHistory.setEditable(false);

        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Kembali ke Dashboard");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        actions.add(btnBack);
        actions.add(btnRefresh);

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                summary,
                new JScrollPane(taHistory)
        );
        split.setResizeWeight(0.3);

        add(title, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> refresh());
        btnBack.addActionListener(e -> { if (onBackDashboard != null) onBackDashboard.run(); });

        refresh();
    }

    private JPanel box(String t, JLabel v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        p.add(new JLabel(" " + t), BorderLayout.NORTH);
        v.setHorizontalAlignment(SwingConstants.CENTER);
        v.setFont(v.getFont().deriveFont(Font.BOLD, 20f));
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    public void refresh() {
        lblTotalBarang.setText(String.valueOf(service.getAll().size()));
        lblTotalStok.setText(String.valueOf(service.totalStok()));
        lblTotalNilai.setText(String.valueOf(service.totalNilai()));
        lblHampirHabis.setText(String.valueOf(service.hampirHabisCount(3)));

        try {
            taHistory.setText(Storage.readHistory());
        } catch (Exception ex) {
            taHistory.setText("Gagal baca history: " + ex.getMessage());
        }
    }
}
