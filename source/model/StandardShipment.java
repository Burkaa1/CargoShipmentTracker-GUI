package com.cargotracker.model;

import java.io.Serializable;

/**
 * Standard shipment: 1.5 TL/km, max 30 kg, insurance = 5 % of cost.
 */
public class StandardShipment extends Shipment implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double RATE_PER_KM   = 1.5;
    private static final double MAX_WEIGHT_KG = 30.0;
    private static final double INSURANCE_PCT = 0.05;

    public StandardShipment(String sender, String recipient, double distanceKm, double weightKg) {
        super(sender, recipient, distanceKm, weightKg);
    }

    @Override public double getRatePerKm()     { return RATE_PER_KM; }
    @Override public double getMaxWeightKg()   { return MAX_WEIGHT_KG; }
    @Override public String typeLabel()        { return "Standard"; }
    @Override public double getInsuranceCost() { return getCost() * INSURANCE_PCT; }
}
