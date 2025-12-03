package fr.imt.springforce.vehicle.business.validators;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleValidator implements Validator<String> {

    private final VehicleRepository vehicleRepository;

    @Override
    public void validate(String matriculation, ValidationResult result) {
        if (vehicleRepository.existsByMatriculation(matriculation)) {
            result.addError("Plaque déjà prise !");
        }
    }
}
