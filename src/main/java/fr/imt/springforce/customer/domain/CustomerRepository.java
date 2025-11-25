package fr.imt.springforce.customer.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findById(UUID id);

    Optional<Customer> findByEmail(String email);

    Collection<Customer> findAll();

    Customer save(Customer customer);

    Optional<Customer> update(Customer customer, UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

}
