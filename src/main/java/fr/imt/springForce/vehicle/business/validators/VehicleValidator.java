package fr.imt.springforce.vehicle.business.validators;

import fr.imt.springforce.vehicle.infrastructure.exceptions.VehicleAlreadyExistsException;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleValidator {

    private final VehicleRepository vehicleRepository;

    public void verifyMatriculationUnicity(String matriculation){
        if (vehicleRepository.existsByMatriculation(matriculation)) {
            throw new VehicleAlreadyExistsException("Plaque déjà prise !");
        }
    }
}
