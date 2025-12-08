package fr.imt.springforce.contract.business.service;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.api.ContractNotFoundException;
import fr.imt.springforce.contract.business.mapper.ContractMapper;
import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import fr.imt.springforce.contract.business.validator.ContractValidator;
import fr.imt.springforce.contract.infrastructure.ContractRepository;
import fr.imt.springforce.vehicle.api.VehicleClient;
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
        validateContract(contractDetails);

        Contract contract = contractMapper.toEntity(contractDetails);

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

    private void validateContract(ContractDetails contractDetails) {
        ValidationChain.of(new ContractValidator(vehicleClient, contractRepository)).validate(contractDetails);
    }

}
