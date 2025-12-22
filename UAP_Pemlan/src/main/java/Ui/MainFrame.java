package Ui;
import Service.InventoryService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout card = new CardLayout();
    private final JPanel content = new JPanel(card);

    private final InventoryService service = new InventoryService();

    private final DashboardPanel dashboard = new DashboardPanel(service);
    private final DataBarangPanel dataBarang = new DataBarangPanel(service);
    private final FormalBarangPanel formBarang = new FormalBarangPanel(service);
    private final LaporanPanel laporan = new LaporanPanel(service);

    public MainFrame() {
        setTitle("Inventaris Toko Kecil");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel menu = new JPanel(new GridLayout(0,1,8,8));
        menu.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JButton btnDash = new JButton("Dashboard");
        JButton btnData = new JButton("Data Barang");
        JButton btnForm = new JButton("Input Barang");
        JButton btnLap  = new JButton("Laporan/History");
        JButton btnExit = new JButton("Keluar");

        menu.add(btnDash); menu.add(btnData); menu.add(btnForm); menu.add(btnLap); menu.add(btnExit);

        content.add(dashboard, "DASH");
        content.add(dataBarang, "DATA");
        content.add(formBarang, "FORM");
        content.add(laporan, "LAP");

        btnDash.addActionListener(e -> { dashboard.refresh(); card.show(content, "DASH"); });
        btnData.addActionListener(e -> { dataBarang.refreshTable(service.getAll()); card.show(content, "DATA"); });
        btnForm.addActionListener(e -> { formBarang.openCreate(); card.show(content, "FORM"); });
        btnLap.addActionListener(e -> { laporan.refresh(); card.show(content, "LAP"); });
        btnExit.addActionListener(e -> System.exit(0));

        // Link panel Data -> Form
        dataBarang.onAdd = () -> { formBarang.openCreate(); card.show(content, "FORM"); };
        dataBarang.onEdit = (kode) -> { formBarang.openEdit(kode); card.show(content, "FORM"); };

        // Link Form -> Data
        formBarang.onSaved = () -> {
            dataBarang.refreshTable(service.getAll());
            dashboard.refresh();
            laporan.refresh();
            card.show(content, "DATA");
        };
        formBarang.onCancel = () -> card.show(content, "DATA");

        // Link Laporan -> Dashboard
        laporan.onBackDashboard = () -> { dashboard.refresh(); card.show(content, "DASH"); };

        setLayout(new BorderLayout());
        add(menu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        dashboard.refresh();
        card.show(content, "DASH");
    }
}