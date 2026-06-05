package com.cargotracker.gui;

import com.cargotracker.model.CargoCompany;

import javax.swing.*;
import java.awt.*;

/**
 * Application launcher for the modern Cargo Shipment Tracker GUI.
 * Sets a professional look-and-feel and launches the main dashboard frame.
 * Pre-loads sample data for immediate demonstration.
 */
public class CargoTrackerGUI {

    public static void main(String[] args) {
        // Set a clean cross-platform look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Optional: improve fonts
            UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 14));
            UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 13));
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            // Create company instance (name can be customized)
            CargoCompany company = new CargoCompany("Istanbul Cargo Logistics");

            // Pre-load demo shipments for immediate usability (like a live demo)
            try {
                company.registerShipment(new com.cargotracker.model.StandardShipment("Ahmet Yılmaz", "Fatma Kaya", 245.0, 18.5));
                company.registerShipment(new com.cargotracker.model.ExpressShipment("Mehmet Demir", "Ayşe Öztürk", 120.0, 9.0));
                company.registerShipment(new com.cargotracker.model.SameDayShipment("Zeynep Çetin", "Ali Vural", 35.0, 4.2));
                company.registerShipment(new com.cargotracker.model.StandardShipment("Burak Şahin", "Elif Yıldız", 310.0, 25.0));
            } catch (Exception e) {
                // Ignore demo data errors
            }

            MainDashboardFrame frame = new MainDashboardFrame(company);
            frame.setVisible(true);
        });
    }
}
