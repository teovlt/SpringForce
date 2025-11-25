package fr.imt.springforce.customer.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface MongoCustomerRepository extends MongoRepository<CustomerDocument, UUID> {

    boolean existsById(UUID id);

    void deleteById(UUID id);

    CustomerDocument findByEmail(String email);
}
