package com.cargotracker.gui.panels;

import com.cargotracker.gui.MainDashboardFrame;
import com.cargotracker.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dialog for advancing shipment status with only valid next states offered.
 */
public class StatusUpdateDialog extends JDialog {

    public StatusUpdateDialog(Frame owner, CargoCompany company, Shipment shipment, ShipmentsPanel shipmentsPanel) {
        super(owner, "Advance Status — ID " + shipment.getId(), true);
        setSize(420, 260);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 24, 10, 24));
        content.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        content.add(new JLabel("Current Status:"), gbc);
        gbc.gridx = 1;
        JLabel current = new JLabel(shipment.getStatus().name());
        current.setFont(new Font("SansSerif", Font.BOLD, 14));
        content.add(current, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("Select Next Status:"), gbc);
        gbc.gridx = 1;

        JComboBox<ShipmentStatus> statusCombo = new JComboBox<>();
        for (ShipmentStatus st : ShipmentStatus.values()) {
            if (shipment.getStatus().canGoTo(st)) {
                statusCombo.addItem(st);
            }
        }
        if (statusCombo.getItemCount() == 0) {
            statusCombo.addItem(shipment.getStatus()); // terminal
        }
        content.add(statusCombo, gbc);

        add(content, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JButton confirm = new JButton("Update Status");
        confirm.setBackground(new Color(59, 130, 246));
        // confirm.setForeground(Color.WHITE);
        confirm.addActionListener(e -> {
            ShipmentStatus next = (ShipmentStatus) statusCombo.getSelectedItem();
            if (next != null && next != shipment.getStatus()) {
                try {
                    shipment.advanceStatus(next);
                    JOptionPane.showMessageDialog(this, "Status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    shipmentsPanel.refresh();
                    dispose();
                } catch (InvalidStatusTransitionException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Transition", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                dispose();
            }
        });

        buttons.add(cancel);
        buttons.add(confirm);
        add(buttons, BorderLayout.SOUTH);
    }
}
