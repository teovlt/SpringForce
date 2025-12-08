package fr.imt.springforce.vehicle.presentation.kafka;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ContractCancellationRequest {

    String vehicleId;

    String reason;

}
