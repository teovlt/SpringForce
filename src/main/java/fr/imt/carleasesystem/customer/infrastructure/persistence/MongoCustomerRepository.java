package fr.imt.carleasesystem.customer.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoCustomerRepository extends MongoRepository<CustomerDocument, UUID> {
}
