package com.cargotracker.gui.panels;

import com.cargotracker.model.Shipment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Read-only detail view for a single shipment. Includes all fields
 * and a simple textual representation of the state machine.
 */
public class ShipmentDetailDialog extends JDialog {

    public ShipmentDetailDialog(Frame owner, Shipment shipment) {
        super(owner, "Shipment Details — ID " + shipment.getId(), true);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(250, 250, 252));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 24, 20, 24));
        content.setBackground(Color.WHITE);

        addField(content, "ID", String.valueOf(shipment.getId()));
        addField(content, "Type", shipment.typeLabel());
        addField(content, "Sender", shipment.getSender());
        addField(content, "Recipient", shipment.getRecipient());
        addField(content, "Distance", String.format("%.1f km", shipment.getDistanceKm()));
        addField(content, "Weight", String.format("%.1f kg", shipment.getWeightKg()));
        addField(content, "Current Status", shipment.getStatus().name());
        addField(content, "Calculated Cost", String.format("%.2f TL", shipment.getCost()));
        addField(content, "Insurance Liability", String.format("%.2f TL", shipment.getInsuranceCost()));

        content.add(Box.createVerticalStrut(16));
        JLabel flowLabel = new JLabel("<html><b>Status Flow (State Machine):</b><br>" +
                "PENDING → IN_TRANSIT or RETURNED<br>" +
                "IN_TRANSIT → DELIVERED or RETURNED<br>" +
                "DELIVERED / RETURNED → Terminal (no further changes)</html>");
        flowLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        content.add(flowLabel);

        add(content, BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(close);
        add(south, BorderLayout.SOUTH);
    }

    private void addField(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        JLabel l = new JLabel(label + ":");
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setPreferredSize(new Dimension(160, 24));
        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.PLAIN, 14));
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        parent.add(row);
        parent.add(Box.createVerticalStrut(4));
    }
}
