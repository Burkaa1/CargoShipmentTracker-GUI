package com.cargotracker.model;

import java.io.Serializable;
import java.util.Locale;

/**
 * Abstract base for every cargo shipment.
 *
 * Owns the shared state (id, sender, recipient, distance, weight, status),
 * the auto-incrementing identifier (via a static counter), the cost formula,
 * and the lifecycle-validated status update. Subclasses supply only the
 * per-kilometre rate, the weight cap, and the type label.
 */
public abstract class Shipment implements Insurable, Serializable {
    private static final long serialVersionUID = 1L;
    private static int counter = 0;            // static ID counter, shared across all subclasses

    protected final int id;
    protected final String sender;
    protected final String recipient;
    protected final double distanceKm;
    protected final double weightKg;
    protected ShipmentStatus status = ShipmentStatus.PENDING;

    protected Shipment(int id, String sender, String recipient, double distanceKm, double weightKg) {
        this.id         = id;
        this.sender     = sender;
        this.recipient  = recipient;
        this.distanceKm = distanceKm;
        this.weightKg   = weightKg;
    }

    protected Shipment(String sender, String recipient, double distanceKm, double weightKg) {
        this(++counter, sender, recipient, distanceKm, weightKg);
    }

    public static void setNextId(int nextId) {
        counter = nextId - 1;
    }

    // ---- subclass-specific values (Standard / Express / SameDay) -----------
    public abstract double getRatePerKm();
    public abstract double getMaxWeightKg();
    public abstract String typeLabel();        // "Standard" | "Express" | "SameDay"

    // ---- shared concrete behaviour -----------------------------------------
    /** Cost = distance (km) x per-km rate (TL/km). */
    public double getCost() { return distanceKm * getRatePerKm(); }

    /**
     * Advance the lifecycle. Delegates the legality check to {@link ShipmentStatus#canGoTo}
     * and throws a typed exception on illegal transitions; otherwise updates the state.
     */
    public void advanceStatus(ShipmentStatus next) {
        if (!status.canGoTo(next)) {
            throw new InvalidStatusTransitionException(status, next);
        }
        this.status = next;
    }

    // ---- getters -----------------------------------------------------------
    public int             getId()            { return id; }
    public String          getSender()        { return sender; }
    public String          getRecipient()     { return recipient; }
    public double          getDistanceKm()    { return distanceKm; }
    public double          getWeightKg()      { return weightKg; }
    public ShipmentStatus  getStatus()        { return status; }

    /**
     * Exact rendering required by the specification:
     *   ID: 1 | [Standard] Mehmet -> Ayse (250.0 km, 12.0 kg) | Cost: 375.00 TL | Status: PENDING
     */
    @Override
    public String toString() {
        return String.format(Locale.US,
                "ID: %d | [%s] %s -> %s (%.1f km, %.1f kg) | Cost: %.2f TL | Status: %s",
                id, typeLabel(), sender, recipient,
                distanceKm, weightKg, getCost(), status);
    }
}
