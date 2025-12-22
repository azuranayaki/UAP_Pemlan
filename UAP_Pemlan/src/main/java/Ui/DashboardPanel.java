package Ui;
import Service.InventoryService;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final InventoryService service;
    private final JLabel lblJenis = new JLabel("0");
    private final JLabel lblStok  = new JLabel("0");

    public DashboardPanel(InventoryService service) {
        this.service = service;
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Dashboard - Inventaris Toko Kecil");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JPanel grid = new JPanel(new GridLayout(1,2,12,12));
        grid.add(box("Total Jenis Barang", lblJenis));
        grid.add(box("Total Stok", lblStok));

        add(title, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }
    private JPanel box(String t, JLabel v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(16,16,16,16)
        ));
        JLabel lt = new JLabel(t);
        lt.setFont(lt.getFont().deriveFont(Font.BOLD, 14f));
        v.setHorizontalAlignment(SwingConstants.CENTER);
        v.setFont(v.getFont().deriveFont(Font.BOLD, 28f));
        p.add(lt, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }
    public void refresh() {
        lblJenis.setText(String.valueOf(service.getAll().size()));
        lblStok.setText(String.valueOf(service.totalStok()));
    }
}