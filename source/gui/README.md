# GUI Layer – Modern Swing Dashboard

Website-inspired professional desktop interface for the Cargo Shipment Tracker.

## Main Components

- **CargoTrackerGUI.java** – Entry point. Sets L&F, pre-loads demo data, launches `MainDashboardFrame`.
- **MainDashboardFrame.java** – Root window with:
  - Dark navy header bar with logo and global refresh
  - Left sidebar navigation (Dashboard / Register / Manage / Reports)
  - `CardLayout` content area
  - Menu bar (File: Save/Load/Export/Exit, View, Help)
  - Status bar
- **panels/**:
  - `DashboardPanel.java` – KPI cards, quick actions, recent shipments
  - `RegisterPanel.java` – Full form + live preview + validation
  - `ShipmentsPanel.java` – Advanced JTable + filter + color status renderer + dialogs + CSV export
  - `ReportsPanel.java` – Summary + type breakdown
  - `ShipmentDetailDialog.java` & `StatusUpdateDialog.java` – Supporting modals

## Design Principles Applied

- Consistent color palette and typography
- Immediate visual feedback (live calculations, colored badges)
- Minimal friction workflows
- Responsive-feeling layouts within desktop constraints
- Clear separation: model is never aware of Swing classes

Run via `CargoTrackerGUI.main(...)`.
