package fr.imt.springforce.vehicle.business.validators;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleValidatorTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleValidator vehicleValidator;

    @Test
    void whenMatriculationIsUnique_shouldNotHaveErrors() {
        String uniqueMatriculation = "ABC-123-DE";
        when(vehicleRepository.existsByMatriculation(uniqueMatriculation)).thenReturn(false);

        ValidationResult result = new ValidationResult();
        vehicleValidator.validate(uniqueMatriculation, result);

        assertThat(result.hasErrors()).isFalse();
        verify(vehicleRepository).existsByMatriculation(uniqueMatriculation);
    }

    @Test
    void whenMatriculationAlreadyExists_shouldHaveError() {
        String existingMatriculation = "XYZ-789-GH";
        when(vehicleRepository.existsByMatriculation(existingMatriculation)).thenReturn(true);

        ValidationResult result = new ValidationResult();
        vehicleValidator.validate(existingMatriculation, result);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("Plaque déjà prise !");
        verify(vehicleRepository).existsByMatriculation(existingMatriculation);
    }
}
