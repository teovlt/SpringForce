package fr.imt.springForce.vehicle.business.service;

import fr.imt.springForce.vehicle.business.model.Vehicle;
import fr.imt.springForce.vehicle.business.validators.VehicleValidator;
import fr.imt.springForce.vehicle.infrastructure.exceptions.VehicleAlreadyExistsException;
import fr.imt.springForce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId).orElse(null);
    }

    public Vehicle create(Vehicle vehicle) {
        vehicleValidator.verifyMatriculationUnicity(vehicle.getMatriculation());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Vehicle vehicle, String vehicleId) {
        Vehicle updatedVehicle = vehicleRepository.findById(vehicleId).orElse(null);

        if (updatedVehicle != null) {
            vehicleValidator.verifyMatriculationUnicity(vehicle.getMatriculation());
            return vehicleRepository.save(updatedVehicle);
        }else{
            return null;
        }
    }

    public void delete(String vehicleId) {
         vehicleRepository.deleteById(vehicleId);
    }

}