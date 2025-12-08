package fr.imt.springforce.contract.business.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
}