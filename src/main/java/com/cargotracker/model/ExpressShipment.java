package com.cargotracker.model;

import java.io.Serializable;

/**
 * Express shipment: 3.0 TL/km, max 20 kg, insurance = 8 % of cost.
 */
public class ExpressShipment extends Shipment implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double RATE_PER_KM   = 3.0;
    private static final double MAX_WEIGHT_KG = 20.0;
    private static final double INSURANCE_PCT = 0.08;

    public ExpressShipment(String sender, String recipient, double distanceKm, double weightKg) {
        super(sender, recipient, distanceKm, weightKg);
    }

    public ExpressShipment(int id, String sender, String recipient, double distanceKm, double weightKg) {
        super(id, sender, recipient, distanceKm, weightKg);
    }

    @Override public double getRatePerKm()     { return RATE_PER_KM; }
    @Override public double getMaxWeightKg()   { return MAX_WEIGHT_KG; }
    @Override public String typeLabel()        { return "Express"; }
    @Override public double getInsuranceCost() { return getCost() * INSURANCE_PCT; }
}
