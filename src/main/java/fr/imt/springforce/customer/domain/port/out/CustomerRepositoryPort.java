package fr.imt.springforce.customer.domain.port.out;

import fr.imt.springforce.customer.domain.Customer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

    Optional<Customer> findById(UUID id);

    Optional<Customer> findByEmail(String email);

    Collection<Customer> findAll();

    Customer save(Customer customer);

    Optional<Customer> update(Customer customer, UUID id);

    boolean existsById(UUID id);

    boolean existsByLicenceNumber(String licenceNumber);

    void deleteById(UUID id);

}
