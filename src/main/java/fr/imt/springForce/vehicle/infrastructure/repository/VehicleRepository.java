package fr.imt.springforce.vehicle.infrastructure.repository;

import fr.imt.springforce.vehicle.business.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    boolean existsByMatriculation(String matriculation);
}
