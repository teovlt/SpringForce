package fr.imt.springforce.vehicle.business.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class Vehicle {
    @Id
    private String id;
    private String brand;
    private String model;
    private String motorization;
    private String color;
    private String matriculation;
    private LocalDate acquisitionDate;
    private VehicleState state;
}
