package com.cargotracker.gui;

import com.cargotracker.gui.panels.*;
import com.cargotracker.model.CargoCompany;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main application window. Provides a modern, website-like dashboard layout:
 * header bar, left sidebar navigation, central content area with CardLayout
 * for seamless panel switching, and a status bar.
 *
 * All panels share the same CargoCompany model instance for data consistency.
 */
public class MainDashboardFrame extends JFrame {

    private final CargoCompany company;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    // Panels
    private DashboardPanel dashboardPanel;
    private RegisterPanel registerPanel;
    private ShipmentsPanel shipmentsPanel;
    private ReportsPanel reportsPanel;

    public MainDashboardFrame(CargoCompany company) {
        this.company = company;
        setTitle("CargoTrack Pro  •  " + company.getCompanyName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1024, 600));

        // Root layout
        setLayout(new BorderLayout(0, 0));

        // ===== HEADER (website-like top bar) =====
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // ===== SIDEBAR + CONTENT =====
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(new Color(245, 247, 250));

        // Sidebar
        JPanel sidebar = createSidebar();
        mainArea.add(sidebar, BorderLayout.WEST);

        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPanel.setBackground(new Color(245, 247, 250));

        // Instantiate panels (pass company reference)
        dashboardPanel = new DashboardPanel(company, this);
        registerPanel  = new RegisterPanel(company, this);
        shipmentsPanel = new ShipmentsPanel(company, this);
        reportsPanel   = new ReportsPanel(company, this);

        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(registerPanel,  "REGISTER");
        contentPanel.add(shipmentsPanel, "SHIPMENTS");
        contentPanel.add(reportsPanel,   "REPORTS");

        mainArea.add(contentPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        // ===== STATUS BAR =====
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        // Show dashboard by default
        cardLayout.show(contentPanel, "DASHBOARD");
        dashboardPanel.refresh();

        // Menu bar
        setJMenuBar(createMenuBar());
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(13, 27, 42)); // Deep navy
        header.setPreferredSize(new Dimension(0, 58));
        header.setBorder(new EmptyBorder(8, 20, 8, 20));

        JLabel logo = new JLabel("🚚  CargoTrack Pro");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Modern Shipment Management  |  Istanbul");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(180, 200, 220));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(logo);
        left.add(subtitle);

        JButton refreshBtn = new JButton("⟳ Refresh All");
        refreshBtn.setFocusPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.setContentAreaFilled(true);
        refreshBtn.setBorderPainted(true);
        refreshBtn.setBackground(new Color(0, 180, 216));
        refreshBtn.setForeground(Color.BLUE);
        refreshBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        refreshBtn.addActionListener(e -> refreshAllPanels());

        header.add(left, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(13, 27, 42));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 12, 20, 12));

        JLabel navTitle = new JLabel("NAVIGATION");
        navTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        navTitle.setForeground(new Color(140, 160, 180));
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navTitle);
        sidebar.add(Box.createVerticalStrut(12));

        String[] navItems = {"Dashboard", "Register Shipment", "Manage Shipments", "Reports & Analytics"};
        String[] cardNames = {"DASHBOARD", "REGISTER", "SHIPMENTS", "REPORTS"};

        for (int i = 0; i < navItems.length; i++) {
            JButton btn = createNavButton(navItems[i], cardNames[i]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("<html><center>EE1004 OOP Project<br>Enhanced GUI Edition</center></html>");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.setForeground(new Color(100, 120, 140));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setRolloverEnabled(true);
        btn.setBackground(new Color(30, 50, 70));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addActionListener(e -> showCard(cardName));

        // Simple hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 180, 216));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 50, 70));
            }
        });
        return btn;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(230, 235, 240));
        bar.setBorder(new EmptyBorder(4, 12, 4, 12));
        bar.setPreferredSize(new Dimension(0, 26));

        JLabel left = new JLabel("Ready  •  Data in memory  •  Professional OOP Implementation");
        left.setFont(new Font("SansSerif", Font.PLAIN, 11));
        left.setForeground(new Color(80, 90, 100));

        JLabel right = new JLabel("Java Swing  |  Model-View-Controller style  |  v2.0 GUI");
        right.setFont(new Font("SansSerif", Font.PLAIN, 11));
        right.setForeground(new Color(100, 110, 120));

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Session...");
        saveItem.addActionListener(e -> saveSession());
        JMenuItem loadItem = new JMenuItem("Load Session...");
        loadItem.addActionListener(e -> loadSession());
        JMenuItem exportItem = new JMenuItem("Export Shipments to CSV...");
        exportItem.addActionListener(e -> shipmentsPanel.exportToCSV());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh All Views");
        refreshItem.addActionListener(e -> refreshAllPanels());
        viewMenu.add(refreshItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About CargoTrack Pro");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        if ("DASHBOARD".equals(cardName)) dashboardPanel.refresh();
        else if ("REGISTER".equals(cardName)) registerPanel.refresh();
        else if ("SHIPMENTS".equals(cardName)) shipmentsPanel.refresh();
        else if ("REPORTS".equals(cardName)) reportsPanel.refresh();
    }

    private void refreshAllPanels() {
        dashboardPanel.refresh();
        shipmentsPanel.refresh();
        reportsPanel.refresh();
        JOptionPane.showMessageDialog(this, "All views refreshed from current data.", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveSession() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("cargotracker-session.dat"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                company.saveToFile(fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Session saved successfully.", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadSession() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                CargoCompany loaded = CargoCompany.loadFromFile(fc.getSelectedFile());
                // For simplicity in this version, we replace by restarting or note limitation.
                // In full production we would swap the model instance.
                JOptionPane.showMessageDialog(this, 
                    "Session loaded. Please restart the application and load the file from the launcher if needed.\n(Advanced model swapping available in future iterations.)", 
                    "Load Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAboutDialog() {
        String msg = "<html><b>CargoTrack Pro</b><br>" +
                "Modern Java Swing GUI for Cargo Shipment Tracking<br><br>" +
                "Original console application enhanced with professional dashboard UI.<br>" +
                "Demonstrates OOP principles + advanced Swing techniques.<br><br>" +
                "EE1004 Object-Oriented Programming – Group Project<br>" +
                "Enhanced GUI Edition for GitHub Portfolio<br><br>" +
                "Features: Real-time validation, color-coded status, interactive tables,<br>" +
                "KPI dashboard, persistence, export, and clean architecture.</html>";
        JOptionPane.showMessageDialog(this, msg, "About CargoTrack Pro", JOptionPane.INFORMATION_MESSAGE);
    }

    // Allow child panels to trigger global refresh easily
    public void requestGlobalRefresh() {
        refreshAllPanels();
    }
}
