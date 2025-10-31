package fr.imt.springForce.vehicle.buisness;

import fr.imt.springForce.vehicle.utils.VehicleState;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@AllArgsConstructor
@Data
public class Vehicle {
    @Id
    private String id;
    private String brand;
    private String model;
    private String motorization;
    private String color;
    private String matriculation;
    private Date acquisitionDate;
    private VehicleState state;
}
