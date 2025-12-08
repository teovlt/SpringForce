package fr.imt.springforce.vehicle.business.validators;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class VehicleValidator implements Validator<String> {

    private final VehicleRepository vehicleRepository;

    private static final Pattern MATRICULATION_PATTERN = Pattern.compile("[A-HJ-NP-TV-Z]{2}[\\s-]{0,1}[0-9]{3}[\\s-]{0,1}[A-HJ-NP-TV-Z]{2}|[0-9]{2,4}[\\s-]{0,1}[A-Z]{1,3}[\\s-]{0,1}[0-9]{2}");

    @Override
    public void validate(String matriculation, ValidationResult result) {
        validateMatriculationNumber(matriculation, result);
        validateMatriculationNumber(matriculation, result);
    }

    public void validateVehicleMatriculationUniqueness(String matriculation, ValidationResult result) {
        if (vehicleRepository.existsByMatriculation(matriculation)) {
            result.addError("Vehicle matriculation number already exist.");
        }
    }

    public void validateMatriculationNumber(String matriculation, ValidationResult result) {
        if (matriculation == null || !MATRICULATION_PATTERN.matcher(matriculation).matches()) {
            result.addError("Invalid matriculation format");
        }
    }

}
