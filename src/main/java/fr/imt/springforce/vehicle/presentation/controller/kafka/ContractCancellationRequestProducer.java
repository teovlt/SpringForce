/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springforce.vehicle.presentation.controller.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractCancellationRequestProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String vehicleId) {
        kafkaTemplate.send("springforce_contract_cancellation_request_private", vehicleId);
    }
}
