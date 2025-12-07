/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springforce.vehicle.infrastructure.kafka;

import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.business.kafka.VehicleStateChange;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleStateChangeListener {

    private final VehicleClient vehicleClient;

    @KafkaListener(topics = "springforce_vehicle_update_state_private", groupId = "car-lease-system")
    public void updateVehicleState(@Payload VehicleStateChange vehicleStateChange) {
        vehicleClient.updateState(vehicleStateChange);
    }

}
