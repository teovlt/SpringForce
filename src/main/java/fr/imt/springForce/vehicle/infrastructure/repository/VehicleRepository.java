package fr.imt.springForce.vehicle.infrastructure.repository;

import fr.imt.springForce.vehicle.business.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
}
