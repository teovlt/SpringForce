package fr.imt.springforce.customer.infrastructure.persistence.adapter;

import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import fr.imt.springforce.customer.infrastructure.persistence.CustomerDocument;
import fr.imt.springforce.customer.infrastructure.persistence.CustomerMongoMapper;
import fr.imt.springforce.customer.infrastructure.persistence.MongoCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MongoCustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerMongoMapper mapper;
    private final MongoCustomerRepository customerRepository;

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findByPublicId(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return Optional.ofNullable(customerRepository.findByEmail(email))
                .map(mapper::toDomain);
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
        return customerRepository.findByPublicId(id)
                .map(existingDocument -> {
                    existingDocument.setFirstName(customer.getFirstName());
                    existingDocument.setFamilyName(customer.getFamilyName());
                    existingDocument.setEmail(customer.getEmail());
                    existingDocument.setPhoneNumber(customer.getPhoneNumber());

                    CustomerDocument updatedDocument = customerRepository.save(existingDocument);
                    return mapper.toDomain(updatedDocument);
                });
    }

    @Override
    public boolean existsById(UUID id) {
        return customerRepository.existsByPublicId(id);
    }

    @Override
    public boolean existsByLicenceNumber(String licenceNumber) {
        return customerRepository.existsByLicenceNumber(licenceNumber);
    }

    @Override
    public void deleteById(UUID id) {
        if (!customerRepository.existsByPublicId(id)) {
            throw new CustomerNotFoundException(String.format("Customer with ID %s not found", id));
        }
        customerRepository.deleteByPublicId(id);
    }

}
