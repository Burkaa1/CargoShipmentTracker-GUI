package com.cargotracker.model;

/**
 * Insurance contract. Every concrete Shipment subclass implements this
 * interface and returns a class-specific percentage of its computed cost.
 */
public interface Insurable {
    /** Insurance liability (in TL) carried by this shipment. */
    double getInsuranceCost();
}
