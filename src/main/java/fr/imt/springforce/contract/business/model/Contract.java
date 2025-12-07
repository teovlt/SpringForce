package fr.imt.springforce.contract.business.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "vehicle_dates_idx",
                def = "{'vehicleId': 1, 'startDate': 1, 'endDate': 1}")
})
public class Contract {

    @Id
    private String id;

    @Indexed
    private String clientId;

    @Indexed
    private String vehicleId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Indexed
    private ContractState status = ContractState.EN_ATTENTE;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime cancelledAt;
    private String cancelReason;
    private LocalDateTime actualReturnDate;

    // Méthodes métier
   /* public boolean isActive() {
        return status == ContractState.EN_COURS || status == ContractState.EN_ATTENTE;
    }*/

    public boolean overlapsWith(LocalDateTime start, LocalDateTime end) {
        return this.startDate.isBefore(end) && this.endDate.isAfter(start);
    }
/*
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(endDate)
                && actualReturnDate == null
                && status == ContractState.EN_COURS;
    }*/

    public void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
}