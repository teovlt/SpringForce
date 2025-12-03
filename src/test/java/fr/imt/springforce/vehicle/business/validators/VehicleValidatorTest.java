package fr.imt.springforce.vehicle.business.validators;

import fr.imt.springforce.vehicle.infrastructure.exceptions.VehicleAlreadyExistsException;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleValidatorTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleValidator vehicleValidator;

    @Test
    void whenMatriculationIsUnique_shouldNotThrowException() {
        String uniqueMatriculation = "ABC-123-DE";
        when(vehicleRepository.existsByMatriculation(uniqueMatriculation)).thenReturn(false);

        vehicleValidator.verifyMatriculationUnicity(uniqueMatriculation);

        verify(vehicleRepository).existsByMatriculation(uniqueMatriculation);
    }

    @Test
    void whenMatriculationAlreadyExists_shouldThrowVehicleAlreadyExistsException() {
        String existingMatriculation = "XYZ-789-GH";
        when(vehicleRepository.existsByMatriculation(existingMatriculation)).thenReturn(true);

        assertThatThrownBy(() -> vehicleValidator.verifyMatriculationUnicity(existingMatriculation))
                .isInstanceOf(VehicleAlreadyExistsException.class)
                .hasMessage("Plaque déjà prise !");

        verify(vehicleRepository).existsByMatriculation(existingMatriculation);
    }
}
