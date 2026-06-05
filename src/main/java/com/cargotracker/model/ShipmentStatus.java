package com.cargotracker.model;

/**
 * Lifecycle states for a Shipment.
 *
 * Legal transitions (state machine):
 *   PENDING    -> IN_TRANSIT | RETURNED
 *   IN_TRANSIT -> DELIVERED  | RETURNED
 *   DELIVERED, RETURNED      -> (terminal: nothing allowed)
 */
public enum ShipmentStatus {
    PENDING, IN_TRANSIT, DELIVERED, RETURNED;

    /**
     * Returns true iff a transition from this state to {@code next} is legal.
     * The check is co-located with the enum so adding a state requires editing
     * only this file.
     */
    public boolean canGoTo(ShipmentStatus next) {
        if (next == null) return false;
        switch (this) {
            case PENDING:
                return next == IN_TRANSIT || next == RETURNED;
            case IN_TRANSIT:
                return next == DELIVERED || next == RETURNED;
            case DELIVERED:
            case RETURNED:
            default:
                return false;
        }
    }
}
