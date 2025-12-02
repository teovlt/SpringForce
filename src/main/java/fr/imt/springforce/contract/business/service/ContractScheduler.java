package fr.imt.springforce.contract.business.service;

import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import fr.imt.springforce.contract.infrastructure.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractScheduler {

    private final ContractRepository contractRepository;

    // Exécuté toutes les heures
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void checkOverdueContracts() {
        log.info("Vérification des contrats en retard...");

        List<Contract> overdueContracts = contractRepository
                .findOverdueContracts(LocalDateTime.now());

        for (Contract contract : overdueContracts) {
/*
            contract.setStatus(ContractState.EN_RETARD);
*/
            contractRepository.save(contract);
            log.warn("Contrat {} passé en retard", contract.getId());

            // Vérifier si ce retard bloque d'autres contrats
            checkAndCancelConflictingContracts(contract);
        }

        log.info("{} contrats passés en retard", overdueContracts.size());
    }

    // Exécuté toutes les 30 minutes
    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void startPendingContracts() {
        log.info("Démarrage des contrats en attente...");

        List<Contract> contractsToStart = contractRepository
                .findContractsToStart(LocalDateTime.now());

        for (Contract contract : contractsToStart) {
/*
            contract.setStatus(ContractState.EN_COURS);
*/
            contractRepository.save(contract);
            log.info("Contrat {} démarré", contract.getId());

            // TODO: Mettre à jour le statut du véhicule
        }

        log.info("{} contrats démarrés", contractsToStart.size());
    }

    private void checkAndCancelConflictingContracts(Contract lateContract) {
        List<Contract> nextContracts = contractRepository
                .findByVehicleIdAndStatusOrderByStartDateAsc(
                        lateContract.getVehicleId(),
                        ContractState.EN_ATTENTE
                );

        for (Contract nextContract : nextContracts) {
            if (nextContract.getStartDate().isBefore(LocalDateTime.now())) {
/*
                nextContract.setStatus(ContractState.ANNULE);
*/
                nextContract.setCancelledAt(LocalDateTime.now());
                nextContract.setCancelReason(
                        "Contrat précédent en retard: " + lateContract.getId()
                );
                contractRepository.save(nextContract);
                log.warn("Contrat {} annulé à cause d'un retard", nextContract.getId());
            }
        }
    }
}