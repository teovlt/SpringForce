package fr.imt.springforce.vehicle.business.service;

import fr.imt.springforce.common.exception.ValidationException;
import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.business.mapper.VehicleMapper;
import fr.imt.springforce.vehicle.business.model.Vehicle;
import fr.imt.springforce.vehicle.business.model.VehicleState;
import fr.imt.springforce.vehicle.business.validators.VehicleValidator;
import fr.imt.springforce.vehicle.infrastructure.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleValidator vehicleValidator;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle testVehicle;
    private VehicleDetails testVehicleDetails;

    @BeforeEach
    void setUp() {
        testVehicle = Vehicle.builder()
                .id("ID123")
                .brand("BrandX")
                .model("ModelY")
                .motorization("Electric")
                .color("Red")
                .matriculation("ABC-123-DE")
                .acquisitionDate(LocalDate.of(2022, 1, 1))
                .state(VehicleState.AVAILABLE)
                .build();

        testVehicleDetails = VehicleDetails.builder()
                .id("ID123")
                .brand("BrandX")
                .model("ModelY")
                .motorization("Electric")
                .color("Red")
                .matriculation("ABC-123-DE")
                .acquisitionDate(LocalDate.of(2022, 1, 1))
                .state(VehicleState.AVAILABLE)
                .build();
    }

    @Test
    void whenFindAll_shouldReturnAllVehicleDetails() {
        List<Vehicle> vehicles = List.of(testVehicle);
        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(vehicleMapper.toDto(any(Vehicle.class))).thenReturn(testVehicleDetails);

        List<VehicleDetails> result = vehicleService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testVehicleDetails);
    }

    @Test
    void whenFindById_withExistingId_shouldReturnVehicleDetails() {
        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));
        when(vehicleMapper.toDto(testVehicle)).thenReturn(testVehicleDetails);

        Optional<VehicleDetails> result = vehicleService.findById(testVehicle.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testVehicleDetails);
    }

    @Test
    void whenFindById_withNonExistingId_shouldReturnEmptyOptional() {
        when(vehicleRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<VehicleDetails> result = vehicleService.findById("nonExistentId");

        assertThat(result).isNotPresent();
    }

    @Test
    void whenCreate_withUniqueMatriculation_shouldReturnSavedVehicleDetails() {
        when(vehicleMapper.toEntity(testVehicleDetails)).thenReturn(testVehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        when(vehicleMapper.toDto(testVehicle)).thenReturn(testVehicleDetails);

        Optional<VehicleDetails> result = vehicleService.create(testVehicleDetails);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testVehicleDetails);
        verify(vehicleValidator).validate(eq(testVehicleDetails.getMatriculation()), any(ValidationResult.class));
        verify(vehicleRepository).save(testVehicle);
    }

    @Test
    void whenCreate_withExistingMatriculation_shouldThrowValidationException() {
        doAnswer(invocation -> {
            ValidationResult result = invocation.getArgument(1);
            result.addError("Plaque déjà prise !");
            return null;
        }).when(vehicleValidator).validate(eq(testVehicleDetails.getMatriculation()), any(ValidationResult.class));

        assertThatThrownBy(() -> vehicleService.create(testVehicleDetails))
                .isInstanceOf(ValidationException.class);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void whenUpdate_withExistingIdAndUniqueMatriculation_shouldReturnUpdatedVehicle() {
        VehicleDetails updatedDetails = VehicleDetails.builder()
                .brand("BrandNew")
                .model("ModelZ")
                .matriculation("NEW-456-MAT")
                .build();

        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));
        when(vehicleMapper.toDto(any(Vehicle.class))).thenReturn(updatedDetails);

        Optional<VehicleDetails> result = vehicleService.update(updatedDetails, testVehicle.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getBrand()).isEqualTo("BrandNew");
        assertThat(result.get().getModel()).isEqualTo("ModelZ");
        assertThat(result.get().getMatriculation()).isEqualTo("NEW-456-MAT");
        verify(vehicleValidator).validate(eq(updatedDetails.getMatriculation()), any(ValidationResult.class));
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void whenUpdate_withExistingMatriculation_shouldThrowValidationException() {
        VehicleDetails updatedDetails = VehicleDetails.builder()
                .matriculation("NEW-456-MAT")
                .build();

        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));
        doAnswer(invocation -> {
            ValidationResult result = invocation.getArgument(1);
            result.addError("Plaque déjà prise !");
            return null;
        }).when(vehicleValidator).validate(eq(updatedDetails.getMatriculation()), any(ValidationResult.class));

        assertThatThrownBy(() -> vehicleService.update(updatedDetails, testVehicle.getId()))
                .isInstanceOf(ValidationException.class);

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void whenUpdate_withNonExistingId_shouldReturnEmptyOptional() {
        VehicleDetails updatedDetails = VehicleDetails.builder().id("nonExistentID").build();
        when(vehicleRepository.findById("nonExistentID")).thenReturn(Optional.empty());

        Optional<VehicleDetails> result = vehicleService.update(updatedDetails, "nonExistentID");

        assertThat(result).isNotPresent();
        verify(vehicleValidator, never()).validate(anyString(), any());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void whenDelete_shouldCallRepositoryDelete() {
        vehicleService.delete(testVehicle.getId());
        verify(vehicleRepository).deleteById(testVehicle.getId());
    }
}
