package fr.imt.springforce.vehicle.api;

public enum VehicleState {
    AVAILABLE("AVAILABLE"),
    IN_LOCATION("IN_LOCATION"),
    OUT_OF_ORDER("OUT_OF_ORDER");

    VehicleState(String state) {
        this.state = state;
    }

    private final String state;
}
