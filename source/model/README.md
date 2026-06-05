# Model Layer – Cargo Shipment Domain

This package contains the complete, packaged, and slightly enhanced original domain model from the console application.

## Classes

- **Insurable.java** – Simple interface for insurance calculation.
- **ShipmentStatus.java** – Enum implementing the state machine with `canGoTo(...)` validation.
- **InvalidStatusTransitionException.java** – Custom unchecked exception.
- **Shipment.java** (abstract) – Core entity with ID generation, cost calculation, `advanceStatus`, getters, `toString`. Now `Serializable`.
- **StandardShipment / ExpressShipment / SameDayShipment** – Concrete implementations with fixed rates, caps, and insurance percentages. `Serializable`.
- **CargoCompany.java** – Central manager (ArrayList + HashMap). Added:
  - `getAllShipments()` for GUI binding
  - `getInsuranceByType(Class)`
  - `saveToFile(File)` / `loadFromFile(File)` for persistence

All original behavior preserved. GUI enhancements are non-breaking.

Use this layer from any presentation (console, GUI, future web, etc.).
