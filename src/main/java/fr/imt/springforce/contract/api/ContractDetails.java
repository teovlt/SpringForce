package fr.imt.springforce.contract.api;

import fr.imt.springforce.contract.business.model.ContractState;
import lombok.Builder;
import lombok.Data;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;

@Data
@Builder
@NamedInterface("ContractDetails")
public class ContractDetails {
    private String id;
    private String clientId;
    private String vehicleId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ContractState status;
}
