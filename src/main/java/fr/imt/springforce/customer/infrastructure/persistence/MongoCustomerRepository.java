package fr.imt.springforce.customer.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoCustomerRepository extends MongoRepository<CustomerDocument, UUID> {
}
