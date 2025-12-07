package fr.imt.springforce.vehicle.business.service;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.business.model.VehicleStateChange;
import fr.imt.springforce.vehicle.business.mapper.VehicleMapper;
import fr.imt.springforce.vehicle.business.model.Vehicle;
import fr.imt.springforce.vehicle.business.model.VehicleState;
import fr.imt.springforce.vehicle.business.validators.VehicleValidator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
class VehicleService implements VehicleClient {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final VehicleMapper vehicleMapper;
    private final ContractClient contractClient;

    @Override
    public List<VehicleDetails> findAll() {
        return vehicleMapper.toDtoList(vehicleRepository.findAll());
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
        log.info("Saved vehicle with id {}", savedVehicle.getId());
        return Optional.of(vehicleMapper.toDto(savedVehicle));
    }

    @Override
    public Optional<VehicleDetails> update(VehicleDetails vehicleDetails, String vehicleId) {
        if (vehicleDetails.getState() == VehicleState.OUT_OF_ORDER) {
            invalidateRelatedContracts(vehicleId);
        }

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

    /**
     * Given a vehicle status modification, will update the
     * @param change vehicle status change notification
     */
    @Override
    public void updateState(VehicleStateChange change) {
        log.info("Vehicle state updated : {} to {}", change.getVehicleId(), change.getState().name());
        // Modify state
        vehicleRepository.findById(change.getVehicleId()).map(vehicle -> {
            vehicle.setState(change.getState());
            return vehicleRepository.save(vehicle);
        });

        // Invalidate out of order contract
        if (change.getState() == VehicleState.OUT_OF_ORDER) {
            invalidateRelatedContracts(change.getVehicleId());
        }
    }

    /**
     * Invalidates contracts related to a vehicleId
     * @param vehicleId vehicleId
     */
    private void invalidateRelatedContracts(String vehicleId) {
        contractClient.getContractsByVehicle(vehicleId).stream().map(ContractDetails::getId)
                .forEach(contractId -> contractClient.cancelContract(contractId, "Vehicle Out of Order"));
    }

}
