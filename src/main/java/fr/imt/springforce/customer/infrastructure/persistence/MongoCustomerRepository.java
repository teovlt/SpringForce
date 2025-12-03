package fr.imt.springforce.customer.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MongoCustomerRepository extends MongoRepository<CustomerDocument, String> {

    Optional<CustomerDocument> findByPublicId(UUID publicId);

    boolean existsByPublicId(UUID publicId);

    void deleteByPublicId(UUID publicId);

    boolean existsByLicenceNumber(String licenceNumber);

    CustomerDocument findByEmail(String email);

    boolean existsByFirstNameAndFamilyNameAndBirthDate(String firstName, String familyName, java.time.Instant birthDate);

    boolean existsByEmail(String email);

}