package fr.imt.springforce.customer.infrastructure.persistence;

import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MongoCustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoMapper mapper;
    private final MongoCustomerRepository customerRepository;

    @Override
    public Optional<Customer> findById(UUID id) {
        CustomerDocument document = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with ID %s not found", id)));
        return Optional.of(mapper.toDomain(document));
    }

    @Override
    public Collection<Customer> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

   @Override
    public Customer save(Customer customer) {
        CustomerDocument document = customerRepository.save(mapper.toDocument(customer));
        return mapper.toDomain(document);
    }

    @Override
    public Optional<Customer> update(Customer customer, UUID id) {
        CustomerDocument document = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with ID %s not found", id)));


        // TODO

        return Optional.empty();
    }

    @Override
    public void delete(UUID id) {

    }

}
