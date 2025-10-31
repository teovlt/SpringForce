package fr.imt.springForce.customer.domain;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findById(UUID id);

    Collection<Customer> findAll();

    Optional<Customer> save(Customer customer);

    Optional<Customer> update(Customer customer, UUID id);

    void delete(UUID id);

}
