package fr.imt.springforce.contract.business.service;

import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import fr.imt.springforce.contract.infrastructure.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;

    public Contract createContract(Contract contract) {
        // Validation des dates
        if (contract.getStartDate().isAfter(contract.getEndDate())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        // Vérifier les chevauchements
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

        // TODO: Vérifier que le véhicule n'est pas en panne (appel au VehicleService)

/*
        contract.setStatus(ContractState.EN_ATTENTE);
*/
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());

        return contractRepository.save(contract);
    }

    public Contract getContractById(String id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrat non trouvé: " + id));
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public List<Contract> getContractsByClient(String clientId) {
        return contractRepository.findByClientId(clientId);
    }

    public List<Contract> getContractsByVehicle(String vehicleId) {
        return contractRepository.findByVehicleId(vehicleId);
    }

    public List<Contract> getContractsByStatus(ContractState status) {
        return contractRepository.findByStatus(status);
    }

    public Contract cancelContract(String contractId, String reason) {
        Contract contract = getContractById(contractId);

       /* if (contract.getStatus() == ContractState.TERMINE ||
                contract.getStatus() == ContractState.ANNULE) {
            throw new IllegalStateException("Ce contrat ne peut pas être annulé");
        }*/

/*
        contract.setStatus(ContractState.ANNULE);
*/
        contract.setCancelledAt(LocalDateTime.now());
        contract.setCancelReason(reason);
        contract.markAsUpdated();

        // TODO: Mettre à jour le statut du véhicule si nécessaire

        return contractRepository.save(contract);
    }

    public Contract completeContract(String contractId, LocalDateTime returnDate) {
        Contract contract = getContractById(contractId);

        /*if (contract.getStatus() != ContractState.EN_COURS &&
                contract.getStatus() != ContractState.EN_RETARD) {
            throw new IllegalStateException("Ce contrat n'est pas en cours");
        }

        contract.setStatus(ContractState.TERMINE);*/
        contract.setActualReturnDate(returnDate);
        contract.markAsUpdated();

        // TODO: Libérer le véhicule (le remettre en DISPONIBLE)

        return contractRepository.save(contract);
    }

    public void cancelPendingContractsForVehicle(String vehicleId, String reason) {
        List<Contract> pendingContracts = contractRepository
                .findByVehicleIdAndStatusOrderByStartDateAsc(vehicleId, ContractState.EN_ATTENTE);

        for (Contract contract : pendingContracts) {
            cancelContract(contract.getId(), reason);
        }
    }
}