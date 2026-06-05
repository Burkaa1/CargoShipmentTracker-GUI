================================================================================
                        CARGO SHIPMENT TRACKER - GUI EDITION
                              EE1004 OOP Project - Spring 2026
================================================================================

PROJECT TITLE      : CargoShipmentTracker - Modern Java Swing GUI
GROUP / AUTHOR     : Atabey Aydı
COURSE             : EE1004 Object Oriented Programming
UNIVERSITY         : Marmara University
SEMESTER           : Spring 2026
DELIVERABLE TYPE   : Enhanced GUI Application + Documentation
GITHUB REPO        : https://github.com/atabeyaydi/CargoShipmentTracker

--------------------------------------------------------------------------------
                                PROJECT OVERVIEW
--------------------------------------------------------------------------------
Original console application demonstrating strong OOP (abstraction, inheritance,
interface, enum state machine, custom exception, Strategy pattern via Comparators,
ArrayList + HashMap storage).

This deliverable adds a complete, professional, website-like Java Swing GUI
dashboard that makes the system production-ready and portfolio-ready.

Key Improvements:
- Modern dashboard with KPI cards and live data
- User-friendly registration form with real-time cost preview & validation
- Interactive sortable/filterable table with color-coded status badges
- Status advancement dialogs enforcing the exact state machine rules
- Reports panel with breakdown
- Persistence (save/load), CSV export, menu system
- Clean packaged architecture + PlantUML diagrams + pseudocode

--------------------------------------------------------------------------------
                                 FILE STRUCTURE
--------------------------------------------------------------------------------
CargoShipmentTracker-GUI/
├── README.md                          # Comprehensive project documentation
├── README.txt                         # This file (projcode schema)
├── CargoShipmentTracker-GUI.zip       # Complete deliverable archive
├── src/main/java/com/cargotracker/
│   ├── model/                         # Packaged domain model (9 classes)
│   └── gui/                           # Full Swing GUI (launcher + frame + 4 panels + 2 dialogs)
├── docs/
│   ├── uml/                           # PlantUML class & architecture diagrams (.puml)
│   └── pseudocode/                    # Execution algorithm pseudocode (.md)
└── Documents/                         # Original academic PDF report

--------------------------------------------------------------------------------
                               HOW TO COMPILE & RUN
--------------------------------------------------------------------------------
1. IDE (IntelliJ / Eclipse / VS Code):
   - Open folder as Java project
   - Run com.cargotracker.gui.CargoTrackerGUI

2. Command Line:
   javac -d out $(find src -name "*.java")
   java -cp out com.cargotracker.gui.CargoTrackerGUI

Application starts with 4 demo shipments pre-loaded.

--------------------------------------------------------------------------------
                              CORE FUNCTIONALITY
--------------------------------------------------------------------------------
All original console features fully supported via GUI:
- Register Standard / Express / Same-Day shipments (with weight cap validation)
- Advance status (only legal transitions allowed)
- List, find, sort (cost / distance / status)
- Revenue & insurance summary (overall + per type)

Additional GUI-only features:
- Live cost/insurance calculator during registration
- Color-coded status in table (PENDING=amber, IN_TRANSIT=blue, DELIVERED=green, RETURNED=red)
- Dynamic filtering and sorting
- Detail view + state machine explanation
- Session save/load (serialization)
- CSV export

--------------------------------------------------------------------------------
                                 DESIGN RATIONALE
--------------------------------------------------------------------------------
- Preserved 100% of original model behavior and OOP contracts
- Added minimal, non-breaking enhancements to CargoCompany (getAllShipments, persistence)
- GUI follows Model-View separation (panels never contain business logic)
- Professional color scheme and layout for "website dashboard" aesthetic
- Extensive inline documentation and subfolder READMEs for maintainability

--------------------------------------------------------------------------------
                              PLANTUML & DIAGRAMS
--------------------------------------------------------------------------------
See docs/uml/ for source files:
- CargoShipmentTracker-ClassDiagram.puml (full model + GUI overview)
- GUI-Architecture.puml (component view)

Render online at plantuml.com/plantuml or with local PlantUML JAR.

--------------------------------------------------------------------------------
                              ACADEMIC COMPLIANCE
--------------------------------------------------------------------------------
- Matches original specification exactly (per-km rates, weight caps, insurance %)
- State machine transitions identical to enum.canGoTo()
- Custom exception used for invalid transitions
- Strong encapsulation, polymorphism (toString, getInsuranceCost), aggregation

For LaTeX report extension (screenshots, GUI design section, updated UML):
Contact maintainer to generate additional .tex content or figures.

================================================================================
                              END OF README.txt
================================================================================
