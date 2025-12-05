package fr.imt.springforce.vehicle.business.service;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.business.mapper.VehicleMapper;
import fr.imt.springforce.vehicle.business.model.Vehicle;
import fr.imt.springforce.vehicle.business.model.VehicleState;
import fr.imt.springforce.vehicle.business.validators.VehicleValidator;
import fr.imt.springforce.vehicle.business.validators.VehicleStateValidator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class VehicleService implements VehicleClient {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final VehicleStateValidator vehicleStateValidator;
    private final VehicleMapper vehicleMapper;

    @Override
    public List<VehicleDetails> findAll() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VehicleDetails> findById(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicleMapper::toDto);
    }

    @Override
    public Optional<VehicleDetails> create(VehicleDetails vehicleDetails) {
        ValidationChain.of(vehicleValidator).validate(vehicleDetails.getMatriculation());
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDetails);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return Optional.of(vehicleMapper.toDto(savedVehicle));
    }

    @Override
    public Optional<VehicleDetails> update(VehicleDetails vehicleDetails, String vehicleId) {
        return vehicleRepository.findById(vehicleId).map(existingVehicle -> {
            if (!Objects.equals(existingVehicle.getMatriculation(), vehicleDetails.getMatriculation())) {
                ValidationChain.of(vehicleValidator).validate(vehicleDetails.getMatriculation());
                existingVehicle.setMatriculation(vehicleDetails.getMatriculation());
            }

            existingVehicle.setBrand(vehicleDetails.getBrand());
            existingVehicle.setModel(vehicleDetails.getModel());
            existingVehicle.setMotorization(vehicleDetails.getMotorization());
            existingVehicle.setColor(vehicleDetails.getColor());
            existingVehicle.setAcquisitionDate(vehicleDetails.getAcquisitionDate());
            existingVehicle.setState(vehicleDetails.getState());

            Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
            return vehicleMapper.toDto(updatedVehicle);
        });
    }

    @Override
    public void delete(String vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public void setVehicleState(VehicleState vehicleState, String vehicleId){
        ValidationChain.of(vehicleStateValidator).validate(vehicleState);
        vehicleRepository.findById(vehicleId).ifPresent(vehicle -> {
            vehicle.setState(vehicleState);
            vehicleRepository.save(vehicle);
        });
    }
}
    