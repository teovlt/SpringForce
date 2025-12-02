package fr.imt.springforce.vehicle.business.service;

import fr.imt.springforce.vehicle.business.model.Vehicle;
import fr.imt.springforce.vehicle.business.model.VehicleState;
import fr.imt.springforce.vehicle.business.validators.VehicleValidator;
import fr.imt.springforce.vehicle.infrastructure.exceptions.VehicleAlreadyExistsException;
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

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle testVehicle;

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
    }

    @Test
    void whenFindAll_shouldReturnAllVehicles() {
        List<Vehicle> vehicles = List.of(testVehicle, Vehicle.builder().id("ID456").build());
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        List<Vehicle> result = vehicleService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(vehicles.toArray(new Vehicle[0]));
    }

    @Test
    void whenFindById_withExistingId_shouldReturnVehicle() {
        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));

        Vehicle result = vehicleService.findById(testVehicle.getId());

        assertThat(result).isEqualTo(testVehicle);
    }

    @Test
    void whenFindById_withNonExistingId_shouldReturnNull() {
        when(vehicleRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        Vehicle result = vehicleService.findById("nonExistentId");

        assertThat(result).isNull();
    }

    @Test
    void whenCreate_withUniqueMatriculation_shouldReturnSavedVehicle() {
        doNothing().when(vehicleValidator).verifyMatriculationUnicity(testVehicle.getMatriculation());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        Vehicle result = vehicleService.create(testVehicle);

        assertThat(result).isEqualTo(testVehicle);
        verify(vehicleValidator).verifyMatriculationUnicity(testVehicle.getMatriculation());
        verify(vehicleRepository).save(testVehicle);
    }

    @Test
    void whenCreate_withExistingMatriculation_shouldThrowException() {
        doThrow(new VehicleAlreadyExistsException("Matriculation already exists")).when(vehicleValidator).verifyMatriculationUnicity(testVehicle.getMatriculation());

        assertThatThrownBy(() -> vehicleService.create(testVehicle))
                .isInstanceOf(VehicleAlreadyExistsException.class)
                .hasMessage("Matriculation already exists");
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void whenUpdate_withExistingId_shouldReturnUpdatedVehicle() {
        Vehicle updatedDetails = Vehicle.builder()
                .id(testVehicle.getId())
                .brand("BrandX")
                .model("ModelZ")
                .color("Blue")
                .state(VehicleState.IN_LOCATION)
                .matriculation("NEW-456-MAT")
                .build();

        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));
        doNothing().when(vehicleValidator).verifyMatriculationUnicity(updatedDetails.getMatriculation());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(updatedDetails);

        Vehicle result = vehicleService.update(updatedDetails, testVehicle.getId());

        assertThat(result).isEqualTo(updatedDetails);
        verify(vehicleValidator).verifyMatriculationUnicity(updatedDetails.getMatriculation());
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void whenUpdate_withNonExistingId_shouldReturnNull() {
        Vehicle updatedDetails = Vehicle.builder().id("nonExistentID").build();
        when(vehicleRepository.findById("nonExistentID")).thenReturn(Optional.empty());

        Vehicle result = vehicleService.update(updatedDetails, "nonExistentID");

        assertThat(result).isNull();
        verify(vehicleValidator, never()).verifyMatriculationUnicity(anyString());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void whenDelete_shouldCallRepositoryDelete() {
        doNothing().when(vehicleRepository).deleteById(testVehicle.getId());

        vehicleService.delete(testVehicle.getId());

        verify(vehicleRepository).deleteById(testVehicle.getId());
    }
}
