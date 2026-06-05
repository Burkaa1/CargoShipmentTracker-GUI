package com.cargotracker.model;

/**
 * Custom unchecked exception thrown by {@link Shipment#advanceStatus} when an
 * illegal state-machine transition is requested.
 *
 * The message format is mandated by the project specification:
 *   "Invalid status transition: <from> -> <to>"
 */
public class InvalidStatusTransitionException extends RuntimeException {
    private final ShipmentStatus fromStatus;
    private final ShipmentStatus toStatus;

    public InvalidStatusTransitionException(ShipmentStatus from, ShipmentStatus to) {
        super("Invalid status transition: " + from + " -> " + to);
        this.fromStatus = from;
        this.toStatus   = to;
    }

    public ShipmentStatus getFromStatus() { return fromStatus; }
    public ShipmentStatus getToStatus()   { return toStatus;   }
}
