# Execution Algorithm – Pseudocode

This document provides clear pseudocode matching the actual source code execution flow for both the original console application and the new GUI edition.

## Original Console Version (Main.java)

```
SET default locale to US (for decimal point)
READ companyName from user input
CREATE CargoCompany with companyName

WHILE true:
    PRINT menu (1-9 options)
    READ user option string

    SWITCH option:
        CASE 1,2,3:
            READ sender, recipient, distance, weight (with try-catch NumberFormat)
            CREATE appropriate Shipment subclass (Standard/Express/SameDay)
            CALL company.registerShipment(s)   // validates weight inside
        CASE 4:
            READ id (try-catch)
            READ desired status string
            FIND shipment by id
            IF found:
                TRY: parse status, CALL shipment.advanceStatus(next)
                CATCH InvalidStatusTransitionException: print message
        CASE 5:
            CALL company.listAllShipments()
        CASE 6:
            READ id
            FIND and PRINT shipment + insurance cost
        CASE 7:
            READ criterion
            CALL company.listSortedBy(criterion)
        CASE 8:
            CALL company.summary()
        CASE 9:
            EXIT loop
        DEFAULT:
            PRINT "Unknown option. Skipping..."
```

## GUI Edition – Event-Driven Flow (Modern Equivalent)

```
LAUNCH CargoTrackerGUI.main():
    SET system LookAndFeel + fonts
    CREATE CargoCompany("Istanbul Cargo Logistics")
    PRE-LOAD 4 demo shipments (registerShipment calls)
    INSTANTIATE MainDashboardFrame(company)
    SHOW frame (header + sidebar + CardLayout content)

USER INTERACTION (event loop handled by Swing):
    CLICK Sidebar Button → cardLayout.show("DASHBOARD" / "REGISTER" etc.)
        → CALL refresh() on target panel

    IN REGISTER PANEL:
        USER changes type / types in fields
            → DocumentListener fires → updatePreview() + updateWeightLimit()
        CLICK "Register Shipment"
            → validate inputs
            → CREATE correct Shipment subclass
            → TRY company.registerShipment(s)
            → SHOW success dialog
            → clearForm()
            → parent.requestGlobalRefresh()  // updates KPIs + table

    IN SHIPMENTS PANEL:
        USER types in search field → filterTable() updates TableModel
        SELECT row + CLICK "Advance Status"
            → OPEN StatusUpdateDialog
                → populate only valid next states via canGoTo()
                → on confirm: shipment.advanceStatus(next)
                → refresh table
        CLICK "View Details" → open ShipmentDetailDialog (read-only + state machine note)

    IN DASHBOARD:
        On show/refresh: recompute KPIs from company totals
        Quick action buttons guide user to other panels

    MENU ACTIONS:
        File > Save Session → JFileChooser + company.saveToFile()
        File > Load Session → company.loadFromFile() (note on restart)
        File > Export CSV → ShipmentsPanel.exportToCSV() writes header + data rows

PERSISTENCE (added):
    CargoCompany implements Serializable
    saveToFile / loadFromFile use ObjectOutputStream / ObjectInputStream
```

The GUI version replaces the blocking `while(running)` menu loop with Swing's event dispatch thread and listener callbacks, providing a far superior user experience while executing identical business logic.
