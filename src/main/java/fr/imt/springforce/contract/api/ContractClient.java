package fr.imt.springforce.contract.api;

import fr.imt.springforce.contract.business.model.ContractState;
import lombok.experimental.NonFinal;
import org.springframework.modulith.NamedInterface;

import java.util.List;
import java.util.Optional;

@NamedInterface("ContractClient")
public interface ContractClient {
    Optional<ContractDetails> createContract(ContractDetails contractDetails);
    Optional<ContractDetails> getContractById(String id);
    List<ContractDetails> getAllContracts();
    List<ContractDetails> getContractsByClient(String clientId);
    List<ContractDetails> getContractsByVehicle(String vehicleId);
    List<ContractDetails> getContractsByStatus(ContractState status);
    Optional<ContractDetails> cancelContract(String contractId, String reason);
}
