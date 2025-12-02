package fr.imt.springforce.contract.infrastructure;

import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractRepository extends MongoRepository<Contract, String> {

    List<Contract> findByClientId(String clientId);

    List<Contract> findByVehicleId(String vehicleId);

    List<Contract> findByStatus(ContractState status);

    @Query("{ 'vehicleId': ?0, 'status': { $in: ['EN_ATTENTE', 'EN_COURS'] }, " +
            "'startDate': { $lt: ?2 }, 'endDate': { $gt: ?1 } }")
    List<Contract> findOverlappingContracts(
            String vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("{ 'status': 'EN_COURS', 'endDate': { $lt: ?0 }, 'actualReturnDate': null }")
    List<Contract> findOverdueContracts(LocalDateTime now);

    @Query("{ 'status': 'EN_ATTENTE', 'startDate': { $lte: ?0 } }")
    List<Contract> findContractsToStart(LocalDateTime now);

    List<Contract> findByVehicleIdAndStatusOrderByStartDateAsc(
            String vehicleId,
            ContractState status
    );
}