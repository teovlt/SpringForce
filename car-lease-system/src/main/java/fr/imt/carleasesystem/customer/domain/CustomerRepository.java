package fr.imt.carleasesystem.customer.domain;

import fr.imt.carleasesystem.customer.api.CustomerDetails;

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
