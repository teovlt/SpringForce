package fr.imt.springforce.vehicle.business.mapper;

import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.business.model.Vehicle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleDetails toDto(Vehicle vehicle);
    Vehicle toEntity(VehicleDetails dto);
    List<VehicleDetails> toDtoList(List<Vehicle> vehicles);
}
