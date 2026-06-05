package com.cargotracker.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Aggregator/manager. Owns every registered {@link Shipment} in two parallel
 * structures: an {@link ArrayList} that preserves insertion order (for listing
 * and sorting) and a {@link HashMap} keyed by tracking ID (for O(1) lookup as
 * mandated by the specification).
 *
 * Exposes three explicit {@link Comparator} strategies on the sort path:
 *   - by cost     (high -> low)
 *   - by distance (high -> low)
 *   - by status   (PENDING first -> terminal last)
 *
 * Enhanced for GUI: added getAllShipments(), saveToFile(...), loadFromFile(...).
 */
public class CargoCompany implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String companyName;
    private final ArrayList<Shipment> shipments          = new ArrayList<>();
    private final HashMap<Integer, Shipment> shipmentById = new HashMap<>();

    // ----- Comparator strategies (Strategy pattern via named constants) -----
    public static final Comparator<Shipment> BY_COST_DESC =
            (a, b) -> Double.compare(b.getCost(), a.getCost());

    public static final Comparator<Shipment> BY_DISTANCE_DESC =
            (a, b) -> Double.compare(b.getDistanceKm(), a.getDistanceKm());

    /** PENDING (0) -> IN_TRANSIT (1) -> DELIVERED (2) -> RETURNED (3). */
    public static final Comparator<Shipment> BY_STATUS_LIFECYCLE =
            Comparator.comparingInt(s -> s.getStatus().ordinal());

    public CargoCompany(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName()   { return companyName; }
    public int    getShipmentCount() { return shipments.size(); }

    /**
     * Returns a defensive copy of all shipments for GUI binding and iteration.
     */
    public List<Shipment> getAllShipments() {
        return new ArrayList<>(shipments);
    }

    // ----- register ---------------------------------------------------------
    public void registerShipment(Shipment s) {
        if (s.getWeightKg() > s.getMaxWeightKg()) {
            // For GUI we throw or return false; here keep console style but GUI will catch
            throw new IllegalArgumentException("Weight exceeds capacity for this shipment class.");
        }
        shipments.add(s);
        shipmentById.put(s.getId(), s);
    }

    // ----- queries ----------------------------------------------------------
    public Shipment findById(int id) {
        return shipmentById.get(id);           // O(1), no linear search
    }

    public void listAllShipments() {  // kept for console compatibility
        for (Shipment s : shipments) {
            System.out.println(s);
        }
    }

    public double getTotalRevenue() {
        double sum = 0.0;
        for (Shipment s : shipments) sum += s.getCost();
        return sum;
    }

    public double getTotalInsurance() {
        double sum = 0.0;
        for (Shipment s : shipments) sum += s.getInsuranceCost();
        return sum;
    }

    // ----- sort & list ------------------------------------------------------
    public void listSortedBy(String criterion) {  // console
        Comparator<Shipment> cmp;
        String header;
        switch (criterion == null ? "" : criterion.trim().toLowerCase(Locale.US)) {
            case "cost":
                cmp    = BY_COST_DESC;
                header = "===== Sorted by Cost (high -> low) =====";
                break;
            case "distance":
                cmp    = BY_DISTANCE_DESC;
                header = "===== Sorted by Distance (high -> low) =====";
                break;
            case "status":
                cmp    = BY_STATUS_LIFECYCLE;
                header = "===== Sorted by Status (PENDING -> RETURNED) =====";
                break;
            default:
                System.out.println("Unknown option. Skipping...");
                return;
        }
        List<Shipment> copy = new ArrayList<>(shipments);
        copy.sort(cmp);
        System.out.println(header);
        for (Shipment s : copy) System.out.println(s);
    }

    // ----- summary ----------------------------------------------------------
    public void summary() {  // console
        double stdIns = 0.0, expIns = 0.0, sdIns = 0.0;
        for (Shipment s : shipments) {
            if      (s instanceof StandardShipment) stdIns += s.getInsuranceCost();
            else if (s instanceof ExpressShipment)  expIns += s.getInsuranceCost();
            else if (s instanceof SameDayShipment)  sdIns  += s.getInsuranceCost();
        }
        System.out.println("===== Revenue & Insurance Summary =====");
        System.out.println("Total Shipments: " + shipments.size());
        System.out.printf(Locale.US, "Total Revenue: %.2f TL%n",   getTotalRevenue());
        System.out.printf(Locale.US, "Total Insurance: %.2f TL%n", getTotalInsurance());
        System.out.printf(Locale.US, "(Standard: %.2f + Express: %.2f + SameDay: %.2f)%n",
                stdIns, expIns, sdIns);
    }

    // ----- GUI-friendly helpers ---------------------------------------------
    public double getInsuranceByType(Class<? extends Shipment> type) {
        double sum = 0.0;
        for (Shipment s : shipments) {
            if (type.isInstance(s)) sum += s.getInsuranceCost();
        }
        return sum;
    }

    // ----- Persistence (Serialization) --------------------------------------
    public void saveToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }

    public static CargoCompany loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (CargoCompany) ois.readObject();
        }
    }
}
