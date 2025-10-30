package fr.imt.carleasesystem.customer.api;

import java.util.*;

public interface CustomerClient {

    Optional<CustomerDetails> findById(UUID id);

    Collection<CustomerDetails> findAll();

    Optional<CustomerDetails> create(CustomerDetails customer);

    Optional<CustomerDetails> update(CustomerDetails customer, UUID id);

    void delete(UUID id);

}
