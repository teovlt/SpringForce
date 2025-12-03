package fr.imt.springforce.vehicle.business.service;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.vehicle.business.model.Vehicle;
import fr.imt.springforce.vehicle.business.validators.VehicleValidator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        ValidationChain.of(vehicleValidator).validate(vehicle.getMatriculation());
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Vehicle vehicle, String vehicleId) {
        return vehicleRepository.findById(vehicleId).map(existingVehicle -> {
            if (!Objects.equals(existingVehicle.getMatriculation(), vehicle.getMatriculation())) {
                ValidationChain.of(vehicleValidator).validate(vehicle.getMatriculation());
                existingVehicle.setMatriculation(vehicle.getMatriculation());
            }

            existingVehicle.setBrand(vehicle.getBrand());
            existingVehicle.setModel(vehicle.getModel());
            existingVehicle.setMotorization(vehicle.getMotorization());
            existingVehicle.setColor(vehicle.getColor());
            existingVehicle.setAcquisitionDate(vehicle.getAcquisitionDate());
            existingVehicle.setState(vehicle.getState());

            return vehicleRepository.save(existingVehicle);
        }).orElse(null);
    }

    public void delete(String vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

}
