/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springforce.contract.presentation.kafka;

import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.vehicle.presentation.kafka.ContractCancellationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleStateChangeListener {

    private final ContractClient contractClient;

    @KafkaListener(topics = "springforce_contract_cancellation_request_private", groupId = "car-lease-system")
    public void updateVehicleState(@Payload ContractCancellationRequest request) {
        contractClient.cancelContract(request.getVehicleId(), request.getReason());
    }

}
