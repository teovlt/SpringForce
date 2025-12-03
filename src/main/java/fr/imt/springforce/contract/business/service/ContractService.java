package fr.imt.springforce.contract.business.service;

import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.business.mapper.ContractMapper;
import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import fr.imt.springforce.contract.infrastructure.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ContractService implements ContractClient {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    @Override
    public Optional<ContractDetails> createContract(ContractDetails contractDetails) {
        Contract contract = contractMapper.toEntity(contractDetails);
        if (contract.getStartDate().isAfter(contract.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
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
        return contractRepository.findAll().stream()
                .map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDetails> getContractsByClient(String clientId) {
        return contractRepository.findByClientId(clientId).stream()
                .map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDetails> getContractsByVehicle(String vehicleId) {
        return contractRepository.findByVehicleId(vehicleId).stream()
                .map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDetails> getContractsByStatus(ContractState status) {
        return contractRepository.findByStatus(status).stream()
                .map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContractDetails> cancelContract(String contractId, String reason) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat non trouvé: " + contractId));

        contract.setCancelledAt(LocalDateTime.now());
        contract.setCancelReason(reason);
        contract.markAsUpdated();

        return Optional.of(contractMapper.toDto(contractRepository.save(contract)));
    }

    private Contract completeContract(String contractId, LocalDateTime returnDate) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat non trouvé: " + contractId));
        contract.setActualReturnDate(returnDate);
        contract.markAsUpdated();
        return contractRepository.save(contract);
    }

    private void cancelPendingContractsForVehicle(String vehicleId, String reason) {
        List<Contract> pendingContracts = contractRepository
                .findByVehicleIdAndStatusOrderByStartDateAsc(vehicleId, ContractState.EN_ATTENTE);

        for (Contract contract : pendingContracts) {
            cancelContract(contract.getId(), reason);
        }
    }
}