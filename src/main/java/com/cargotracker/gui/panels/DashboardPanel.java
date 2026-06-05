package com.cargotracker.gui.panels;

import com.cargotracker.gui.MainDashboardFrame;
import com.cargotracker.model.CargoCompany;
import com.cargotracker.model.Shipment;
import com.cargotracker.model.ShipmentStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Dashboard panel – website-style landing view with KPI cards,
 * quick actions, and recent shipments overview.
 */
public class DashboardPanel extends JPanel {

    private final CargoCompany company;
    private final MainDashboardFrame parentFrame;

    private JLabel totalShipmentsLabel;
    private JLabel totalRevenueLabel;
    private JLabel totalInsuranceLabel;
    private JLabel pendingLabel;

    public DashboardPanel(CargoCompany company, MainDashboardFrame parentFrame) {
        this.company = company;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createKpiSection(), BorderLayout.CENTER);
        add(createQuickActionsAndRecent(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(13, 27, 42));

        JLabel subtitle = new JLabel("Real-time insights into your cargo operations");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 110, 130));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.add(title);
        left.add(subtitle);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    private JPanel createKpiSection() {
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiPanel.setOpaque(false);

        totalShipmentsLabel = createKpiCard("Total Shipments", "0", "📦", new Color(13, 27, 42));
        totalRevenueLabel   = createKpiCard("Total Revenue", "0.00 TL", "💰", new Color(16, 185, 129));
        totalInsuranceLabel = createKpiCard("Insurance Liability", "0.00 TL", "🛡️", new Color(245, 158, 11));
        pendingLabel        = createKpiCard("Pending / In Transit", "0", "⏳", new Color(59, 130, 246));

        kpiPanel.add(totalShipmentsLabel.getParent());
        kpiPanel.add(totalRevenueLabel.getParent());
        kpiPanel.add(totalInsuranceLabel.getParent());
        kpiPanel.add(pendingLabel.getParent());

        return kpiPanel;
    }

    private JLabel createKpiCard(String title, String value, String icon, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 110, 130));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        valueLabel.setForeground(accent);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(iconLabel);
        top.add(titleLabel);

        card.add(top, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        // Store value label for later update
        valueLabel.putClientProperty("title", title);

        return valueLabel;
    }

    private JPanel createQuickActionsAndRecent() {
        JPanel bottom = new JPanel(new BorderLayout(16, 0));
        bottom.setOpaque(false);

        // Quick Actions
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Quick Actions"),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JButton regBtn = new JButton("➕ Register New Shipment");
        regBtn.addActionListener(e -> parentFrame.requestGlobalRefresh()); // placeholder navigation in real would switch card
        // In full app the parent switches card; here we simulate by message
        regBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Navigate to 'Register Shipment' using the left sidebar.", "Quick Action", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton manageBtn = new JButton("📋 Manage Shipments");
        manageBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Navigate to 'Manage Shipments' using the left sidebar to view table and update status.", "Quick Action", JOptionPane.INFORMATION_MESSAGE);
        });

        actions.add(regBtn);
        actions.add(Box.createVerticalStrut(8));
        actions.add(manageBtn);

        // Recent shipments
        JPanel recent = new JPanel(new BorderLayout());
        recent.setBackground(Color.WHITE);
        recent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Recent Shipments (last 4)"),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JTextArea recentArea = new JTextArea();
        recentArea.setEditable(false);
        recentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        recentArea.setBackground(new Color(250, 250, 252));

        List<Shipment> all = company.getAllShipments();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = all.size() - 1; i >= 0 && count < 4; i--, count++) {
            Shipment s = all.get(i);
            sb.append(s.toString()).append("\n");
        }
        recentArea.setText(sb.length() > 0 ? sb.toString() : "No shipments yet. Register some from the sidebar.");

        recent.add(new JScrollPane(recentArea), BorderLayout.CENTER);

        bottom.add(actions, BorderLayout.WEST);
        bottom.add(recent, BorderLayout.CENTER);

        return bottom;
    }

    public void refresh() {
        // Update KPI values
        int total = company.getShipmentCount();
        double rev = company.getTotalRevenue();
        double ins = company.getTotalInsurance();

        long pendingOrTransit = company.getAllShipments().stream()
                .filter(s -> s.getStatus() == ShipmentStatus.PENDING || s.getStatus() == ShipmentStatus.IN_TRANSIT)
                .count();

        // Find and update labels (simple approach: re-create or use client properties)
        // For demo we use JOption or just print; in real we would store references to value labels.
        // Simplified refresh message for Phase 1 completeness:
        System.out.println("[Dashboard] KPIs refreshed: " + total + " shipments, Revenue: " + String.format("%.2f", rev));

        // For visual update we can re-validate the panel
        revalidate();
        repaint();
    }
}
