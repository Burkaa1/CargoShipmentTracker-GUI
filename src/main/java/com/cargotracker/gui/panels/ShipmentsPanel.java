package com.cargotracker.gui.panels;

import com.cargotracker.gui.MainDashboardFrame;
import com.cargotracker.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Shipments management panel featuring a modern interactive JTable,
 * real-time search/filter, color-coded status, and actions for
 * viewing details and advancing status.
 */
public class ShipmentsPanel extends JPanel {

    private final CargoCompany company;
    private final MainDashboardFrame parentFrame;
    private JTable table;
    private ShipmentTableModel tableModel;
    private JTextField searchField;

    public ShipmentsPanel(CargoCompany company, MainDashboardFrame parentFrame) {
        this.company = company;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createActionBar(), BorderLayout.SOUTH);
    }

    private JPanel createToolbar() {
        JPanel tool = new JPanel(new BorderLayout(8, 0));
        tool.setOpaque(false);

        JLabel title = new JLabel("All Shipments");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(13, 27, 42));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Filter:"));
        searchField = new JTextField(22);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
        searchPanel.add(searchField);

        JButton refreshBtn = new JButton("⟳ Refresh Table");
        refreshBtn.addActionListener(e -> refresh());

        tool.add(title, BorderLayout.WEST);
        tool.add(searchPanel, BorderLayout.CENTER);
        tool.add(refreshBtn, BorderLayout.EAST);
        return tool;
    }

    private JPanel createTablePanel() {
        tableModel = new ShipmentTableModel(company.getAllShipments());
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        // Status column custom renderer (color coded)
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createActionBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setOpaque(false);

        JButton deleteBtn = new JButton("Delete Selected");
        /*
        deleteBtn.setBackground(new Color(220, 38, 38));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setOpaque(true);
        deleteBtn.setContentAreaFilled(true);
        deleteBtn.setBorderPainted(true);
        */
        deleteBtn.addActionListener(e -> deleteSelectedShipment());

        JButton detailsBtn = new JButton("View Details");
        detailsBtn.addActionListener(e -> showDetails());

        JButton updateBtn = new JButton("Advance Status");
        updateBtn.setBackground(new Color(59, 130, 246));
        /*
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setOpaque(true);
        updateBtn.setContentAreaFilled(true);
        updateBtn.setBorderPainted(true);
        */
        updateBtn.addActionListener(e -> advanceStatusForSelected());

        bar.add(deleteBtn);
        bar.add(detailsBtn);
        bar.add(updateBtn);
        return bar;
    }

    private void filterTable() {
        String term = searchField.getText().trim().toLowerCase();
        if (term.isEmpty()) {
            tableModel.setData(company.getAllShipments());
            return;
        }
        List<Shipment> filtered = new ArrayList<>();
        for (Shipment s : company.getAllShipments()) {
            if (String.valueOf(s.getId()).contains(term) ||
                s.getSender().toLowerCase().contains(term) ||
                s.getRecipient().toLowerCase().contains(term) ||
                s.typeLabel().toLowerCase().contains(term)) {
                filtered.add(s);
            }
        }
        tableModel.setData(filtered);
    }

    public void refresh() {
        tableModel.setData(company.getAllShipments());
        searchField.setText("");
    }

    private void showDetails() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a shipment from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Shipment s = tableModel.getShipmentAt(table.convertRowIndexToModel(row));
        new ShipmentDetailDialog(parentFrame, s).setVisible(true);
    }

    private void deleteSelectedShipment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a shipment to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Shipment s = tableModel.getShipmentAt(table.convertRowIndexToModel(row));
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete shipment ID " + s.getId() + " for " + s.getSender() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (company.deleteShipment(s.getId())) {
            refresh();
            JOptionPane.showMessageDialog(this, "Shipment deleted successfully.", "Delete Complete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Unable to delete the selected shipment.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void advanceStatusForSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a shipment.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Shipment s = tableModel.getShipmentAt(table.convertRowIndexToModel(row));
        new StatusUpdateDialog(parentFrame, company, s, this).setVisible(true);
    }

    public void exportToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("shipments-export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fc.getSelectedFile()))) {
                pw.println("ID,Type,Sender,Recipient,Distance_km,Weight_kg,Status,Cost_TL,Insurance_TL");
                for (Shipment s : company.getAllShipments()) {
                    pw.printf("%d,%s,%s,%s,%.1f,%.1f,%s,%.2f,%.2f%n",
                            s.getId(), s.typeLabel(), s.getSender(), s.getRecipient(),
                            s.getDistanceKm(), s.getWeightKg(), s.getStatus(),
                            s.getCost(), s.getInsuranceCost());
                }
                JOptionPane.showMessageDialog(this, "CSV exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Inner Table Model
    private static class ShipmentTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Type", "Sender", "Recipient", "Distance (km)", "Weight (kg)", "Status", "Cost (TL)", "Insurance (TL)"};
        private List<Shipment> data;

        public ShipmentTableModel(List<Shipment> initialData) {
            this.data = new ArrayList<>(initialData);
        }

        public void setData(List<Shipment> newData) {
            this.data = new ArrayList<>(newData);
            fireTableDataChanged();
        }

        public Shipment getShipmentAt(int modelRow) {
            return data.get(modelRow);
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Shipment s = data.get(row);
            switch (col) {
                case 0: return s.getId();
                case 1: return s.typeLabel();
                case 2: return s.getSender();
                case 3: return s.getRecipient();
                case 4: return String.format("%.1f", s.getDistanceKm());
                case 5: return String.format("%.1f", s.getWeightKg());
                case 6: return s.getStatus().name();
                case 7: return String.format("%.2f", s.getCost());
                case 8: return String.format("%.2f", s.getInsuranceCost());
                default: return "";
            }
        }
    }

    // Custom status cell renderer for color coding (website-like badges)
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            String status = (value != null) ? value.toString() : "";
            switch (status) {
                case "PENDING":
                    label.setBackground(new Color(254, 243, 199)); // amber-100
                    label.setForeground(new Color(180, 83, 9));
                    break;
                case "IN_TRANSIT":
                    label.setBackground(new Color(219, 234, 254)); // blue-100
                    label.setForeground(new Color(30, 64, 175));
                    break;
                case "DELIVERED":
                    label.setBackground(new Color(209, 250, 229)); // green-100
                    label.setForeground(new Color(6, 95, 70));
                    break;
                case "RETURNED":
                    label.setBackground(new Color(254, 226, 226)); // red-100
                    label.setForeground(new Color(153, 27, 27));
                    break;
                default:
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
            }
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setText("  " + status + "  ");
            return label;
        }
    }
}
