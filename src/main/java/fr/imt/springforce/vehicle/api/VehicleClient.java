package fr.imt.springforce.vehicle.api;

import org.springframework.modulith.NamedInterface;

import java.util.List;
import java.util.Optional;

@NamedInterface("VehicleClient")
public interface VehicleClient {
    List<VehicleDetails> findAll();
    Optional<VehicleDetails> findById(String vehicleId);
    Optional<VehicleDetails> create(VehicleDetails vehicleDetails);
    Optional<VehicleDetails> update(VehicleDetails vehicleDetails, String vehicleId);
    void delete(String vehicleId);
}
