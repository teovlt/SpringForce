package fr.imt.springforce.vehicle.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDate;

@Data
@Builder
@NamedInterface("VehicleDetails")
public class VehicleDetails {
    private String id;
    private String brand;
    private String model;
    private String motorization;
    private String color;
    private String matriculation;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate acquisitionDate;
    private VehicleState state;
}
