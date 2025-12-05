package fr.imt.springforce.vehicle.business.validators;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;
import fr.imt.springforce.vehicle.business.model.VehicleState;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VehicleStateValidator implements Validator<VehicleState> {

    private static final Set<VehicleState> VALID_STATES = Arrays.stream(VehicleState.values())
            .collect(Collectors.toSet());

    @Override
    public void validate(VehicleState vehicleState, ValidationResult result) {
        if (vehicleState == null) {
            result.addError("L'état du véhicule ne peut pas être null");
            return;
        }
        
        if (!VALID_STATES.contains(vehicleState)) {
            result.addError("L'état du véhicule n'existe pas dans l'enum");
        }
    }
}

