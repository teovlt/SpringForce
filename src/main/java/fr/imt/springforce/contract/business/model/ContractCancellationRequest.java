package fr.imt.springforce.contract.business.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ContractCancellationRequest {

    String vehicleId;

    String reason;

}
