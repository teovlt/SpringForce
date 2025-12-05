package fr.imt.springforce.vehicle.api;

import fr.imt.springforce.vehicle.business.model.VehicleState;

import java.util.List;
import java.util.Optional;

public interface VehicleClient {
    List<VehicleDetails> findAll();
    Optional<VehicleDetails> findById(String vehicleId);
    Optional<VehicleDetails> create(VehicleDetails vehicleDetails);
    Optional<VehicleDetails> update(VehicleDetails vehicleDetails, String vehicleId);
    void delete(String vehicleId);
    void setVehicleState(VehicleState vehicleState, String vehicleId);
}
