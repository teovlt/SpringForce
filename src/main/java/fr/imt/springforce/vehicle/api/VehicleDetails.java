package fr.imt.springforce.vehicle.api;

import fr.imt.springforce.vehicle.business.model.VehicleState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VehicleDetails {
    private String id;
    private String brand;
    private String model;
    private String motorization;
    private String color;
    private String matriculation;
    private LocalDate acquisitionDate;
    private VehicleState state;
}
