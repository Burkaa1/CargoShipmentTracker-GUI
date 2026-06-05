package com.cargotracker.gui.panels;

import com.cargotracker.gui.MainDashboardFrame;
import com.cargotracker.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Reports & Analytics panel showing summary statistics and type breakdown.
 */
public class ReportsPanel extends JPanel {

    private final CargoCompany company;
    private final MainDashboardFrame parentFrame;

    private JLabel totalLabel, revenueLabel, insuranceLabel;
    private JTextArea breakdownArea;

    public ReportsPanel(CargoCompany company, MainDashboardFrame parentFrame) {
        this.company = company;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createSummaryCards(), BorderLayout.CENTER);
        add(createBreakdown(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        h.add(title, BorderLayout.WEST);
        return h;
    }

    private JPanel createSummaryCards() {
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);

        totalLabel = createBigStatCard("Total Shipments", "0");
        revenueLabel = createBigStatCard("Total Revenue", "0.00 TL");
        insuranceLabel = createBigStatCard("Total Insurance", "0.00 TL");

        cards.add(totalLabel.getParent());
        cards.add(revenueLabel.getParent());
        cards.add(insuranceLabel.getParent());

        return cards;
    }

    private JLabel createBigStatCard(String title, String initialValue) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setForeground(new Color(100, 110, 130));

        JLabel v = new JLabel(initialValue);
        v.setFont(new Font("SansSerif", Font.BOLD, 28));
        v.setForeground(new Color(13, 27, 42));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);

        v.putClientProperty("stat", title);
        return v;
    }

    private JPanel createBreakdown() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Insurance Breakdown by Type"),
                new EmptyBorder(12, 12, 12, 12)
        ));

        breakdownArea = new JTextArea(6, 50);
        breakdownArea.setEditable(false);
        breakdownArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        breakdownArea.setBackground(new Color(250, 250, 252));

        p.add(new JScrollPane(breakdownArea), BorderLayout.CENTER);
        return p;
    }

    public void refresh() {
        int total = company.getShipmentCount();
        double rev = company.getTotalRevenue();
        double ins = company.getTotalInsurance();

        totalLabel.setText(String.valueOf(total));
        revenueLabel.setText(String.format("%.2f TL", rev));
        insuranceLabel.setText(String.format("%.2f TL", ins));

        double std = company.getInsuranceByType(StandardShipment.class);
        double exp = company.getInsuranceByType(ExpressShipment.class);
        double sd  = company.getInsuranceByType(SameDayShipment.class);

        String text = String.format(
                "Standard Shipments : %.2f TL insurance\n" +
                "Express Shipments  : %.2f TL insurance\n" +
                "Same-Day Shipments : %.2f TL insurance\n\n" +
                "Note: Insurance is calculated as a percentage of each shipment's cost.",
                std, exp, sd);
        breakdownArea.setText(text);
    }
}
