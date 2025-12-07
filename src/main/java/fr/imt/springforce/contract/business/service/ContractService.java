package fr.imt.springforce.contract.business.service;

import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.api.ContractNotFoundException;
import fr.imt.springforce.contract.business.mapper.ContractMapper;
import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import fr.imt.springforce.contract.infrastructure.ContractRepository;
import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.api.VehicleState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class ContractService implements ContractClient {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final VehicleClient vehicleClient;

    @Override
    public Optional<ContractDetails> createContract(ContractDetails contractDetails) {
        Contract contract = contractMapper.toEntity(contractDetails);
        if (contract.getStartDate().isAfter(contract.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        if (vehicleClient.findById(contractDetails.getVehicleId()).map(VehicleDetails::getState).orElse(VehicleState.AVAILABLE) == VehicleState.OUT_OF_ORDER) {
            throw new IllegalStateException("Le vehicule actuel est en panne");
        }

        List<Contract> overlapping = contractRepository.findOverlappingContracts(
                contract.getVehicleId(),
                contract.getStartDate(),
                contract.getEndDate()
        );

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException(
                    "Le véhicule est déjà réservé sur cette période"
            );
        }
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());

        return Optional.of(contractMapper.toDto(contractRepository.save(contract)));
    }

    @Override
    public Optional<ContractDetails> getContractById(String id) {
        return contractRepository.findById(id).map(contractMapper::toDto);
    }

    @Override
    public List<ContractDetails> getAllContracts() {
        return contractMapper.toDtoList(contractRepository.findAll());
    }

    @Override
    public List<ContractDetails> getContractsByClient(String clientId) {
        return contractMapper.toDtoList(contractRepository.findByClientId(clientId));
    }

    @Override
    public List<ContractDetails> getContractsByVehicle(String vehicleId) {
        return contractMapper.toDtoList(contractRepository.findByVehicleId(vehicleId));
    }

    @Override
    public List<ContractDetails> getContractsByStatus(ContractState status) {
        return contractMapper.toDtoList(contractRepository.findByStatus(status));
    }

    @Override
    public Optional<ContractDetails> cancelContract(String contractId, String reason) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(contractId));

        contract.setCancelledAt(LocalDateTime.now());
        contract.setCancelReason(reason);
        contract.markAsUpdated();

        return Optional.of(contractMapper.toDto(contractRepository.save(contract)));
    }

}