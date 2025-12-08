package fr.imt.springforce.vehicle.business.service;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.presentation.kafka.ContractCancellationRequest;
import fr.imt.springforce.vehicle.business.mapper.VehicleMapper;
import fr.imt.springforce.vehicle.business.model.Vehicle;
import fr.imt.springforce.vehicle.api.VehicleState;
import fr.imt.springforce.vehicle.business.validators.VehicleValidator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import fr.imt.springforce.vehicle.presentation.kafka.ContractCancellationRequestProducer;
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
    private final VehicleMapper vehicleMapper;
    private final ContractCancellationRequestProducer contractCancellationRequestProducer;

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
        ValidationChain.of(new VehicleValidator(vehicleRepository)).validate(vehicleDetails.getMatriculation());

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDetails);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Saved vehicle with id {}", savedVehicle.getId());
        return Optional.of(vehicleMapper.toDto(savedVehicle));
    }

    @Override
    public Optional<VehicleDetails> update(VehicleDetails vehicleDetails, String vehicleId) {
        if (VehicleState.OUT_OF_ORDER == vehicleDetails.getState()) {
            invalidateRelatedContracts(vehicleId, VehicleState.OUT_OF_ORDER.name());
        }

        return vehicleRepository.findById(vehicleId).map(existingVehicle -> {
            if (!Objects.equals(existingVehicle.getMatriculation(), vehicleDetails.getMatriculation())) {
                ValidationChain.of(new VehicleValidator(vehicleRepository)).validate(vehicleDetails.getMatriculation());
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
     * Invalidates contracts related to a vehicleId
     * @param vehicleId vehicleId
     */
    private void invalidateRelatedContracts(String vehicleId, String reason) {
        contractCancellationRequestProducer
                .send(ContractCancellationRequest.builder().vehicleId(vehicleId)
                        .reason(reason)
                        .build());
    }

}
