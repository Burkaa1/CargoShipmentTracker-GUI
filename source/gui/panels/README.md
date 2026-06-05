# GUI Panels & Dialogs

This package holds all visual components.

- **DashboardPanel**: KPI overview cards and recent activity.
- **RegisterPanel**: Form with `DocumentListener` live updates and capacity validation.
- **ShipmentsPanel**: Core data table (`ShipmentTableModel` inner class) + `StatusCellRenderer` for beautiful status badges.
- **ReportsPanel**: Aggregated metrics and breakdown.
- **ShipmentDetailDialog** & **StatusUpdateDialog**: Focused interactions that enforce business rules.

All panels receive a shared `CargoCompany` reference for data access and mutation.
