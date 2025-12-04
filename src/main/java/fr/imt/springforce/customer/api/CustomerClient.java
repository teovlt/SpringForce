package fr.imt.springforce.customer.api;

import org.springframework.modulith.NamedInterface;

import java.util.*;

@NamedInterface("CustomerClient")
public interface CustomerClient {

    Optional<CustomerDetails> findById(UUID id);

    Collection<CustomerDetails> findAll();

    Optional<CustomerDetails> save(CustomerDetails customer);

    Optional<CustomerDetails> update(CustomerDetails customer, UUID id);

    void delete(UUID id);

}
