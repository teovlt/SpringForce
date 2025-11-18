package fr.imt.springForce.vehicle.business.service;

import fr.imt.springForce.vehicle.business.model.Vehicle;
import fr.imt.springForce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId).orElse(null);
    }

    public Vehicle create(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Vehicle vehicle, String vehicleId) {
        Vehicle updatedVehicle = vehicleRepository.findById(vehicleId).orElse(null);

        if (updatedVehicle != null) {
            return vehicleRepository.save(updatedVehicle);
        }else{
            return null;
        }
    }

    public void delete(String vehicleId) {
         vehicleRepository.deleteById(vehicleId);
    }

}