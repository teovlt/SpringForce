package fr.imt.springforce.customer.application;

import fr.imt.springforce.common.exception.ValidationException;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.application.mapper.CustomerMapper;
import fr.imt.springforce.customer.domain.Address;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.CustomerId;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDetails defaultCustomerDetails;
    private Customer defaultCustomer;
    private Address defaultAddress;
    private Instant defaultBirthDate;

    @BeforeEach
    void setUp() {
        defaultAddress = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("75000")
                .city("Paris")
                .countryCode("FR")
                .build();

        defaultBirthDate = Instant.parse("1990-01-15T10:00:00Z");

        defaultCustomerDetails = CustomerDetails.builder()
                .firstName("John")
                .familyName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address(defaultAddress)
                .licenceNumber("12012A123456")
                .birthDate(defaultBirthDate)
                .build();

        defaultCustomer = Customer.builder()
                .customerId(CustomerId.generate())
                .firstName(defaultCustomerDetails.getFirstName())
                .familyName(defaultCustomerDetails.getFamilyName())
                .email(defaultCustomerDetails.getEmail())
                .phoneNumber(defaultCustomerDetails.getPhoneNumber())
                .address(defaultCustomerDetails.getAddress())
                .licenceNumber(defaultCustomerDetails.getLicenceNumber())
                .birthDate(defaultCustomerDetails.getBirthDate())
                .build();
    }

    @Test
    void whenSaveNewCustomer_shouldReturnSavedCustomerDetails() {
        when(customerRepositoryPort.save(any(Customer.class))).thenReturn(defaultCustomer);
        when(customerMapper.toCustomerDetails(defaultCustomer)).thenReturn(defaultCustomerDetails);

        Optional<CustomerDetails> result = customerService.save(defaultCustomerDetails);

        assertThat(result).isPresent().contains(defaultCustomerDetails);
        verify(customerRepositoryPort).save(any(Customer.class));
    }

    @Test
    void whenCreate_withDuplicateNameAndBirthDate_shouldThrowValidationException() {
        when(customerRepositoryPort.existsByFirstNameAndFamilyNameAndBirthDate(
                defaultCustomerDetails.getFirstName(),
                defaultCustomerDetails.getFamilyName(),
                defaultCustomerDetails.getBirthDate()
        )).thenReturn(true);

        assertThatThrownBy(() -> customerService.save(defaultCustomerDetails))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("A user with the same first name, last name, and birth date already exists.");

        verify(customerRepositoryPort, never()).save(any(Customer.class));
    }

    @Test
    void whenFindById_withExistingId_shouldReturnCustomerDetails() {
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.builder()
                .customerId(CustomerId.generate())
                .firstName("Jane")
                .familyName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("0987654321")
                .address(defaultAddress)
                .licenceNumber("12012A123456")
                .birthDate(defaultBirthDate)
                .build();
        CustomerDetails expectedDetails = CustomerDetails.builder()
                .firstName("Jane")
                .familyName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("0987654321")
                .birthDate(defaultBirthDate)
                .build();

        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerDetails(customer)).thenReturn(expectedDetails);

        Optional<CustomerDetails> result = customerService.findById(customerId);

        assertThat(result).isPresent().contains(expectedDetails);
    }

    @Test
    void whenFindById_withNonExistingId_shouldReturnEmptyOptional() {
        UUID customerId = UUID.randomUUID();
        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDetails> result = customerService.findById(customerId);

        assertThat(result).isNotPresent();
    }

    @Test
    void whenFindAll_shouldReturnAllCustomerDetails() {
        Customer customer1 = defaultCustomer;
        Customer customer2 = Customer.builder()
                .customerId(CustomerId.generate())
                .firstName("Jane")
                .familyName("Smith")
                .email("jane.smith@example.com")
                .phoneNumber("0987654321")
                .address(defaultAddress)
                .licenceNumber("12012A123456")
                .birthDate(defaultBirthDate)
                .build();
        List<Customer> customers = List.of(customer1, customer2);

        CustomerDetails details1 = defaultCustomerDetails;
        CustomerDetails details2 = CustomerDetails.builder()
                .firstName("Jane")
                .familyName("Smith")
                .email("jane.smith@example.com")
                .phoneNumber("0987654321")
                .birthDate(defaultBirthDate)
                .build();

        when(customerRepositoryPort.findAll()).thenReturn(customers);
        when(customerMapper.toCustomerDetails(customer1)).thenReturn(details1);
        when(customerMapper.toCustomerDetails(customer2)).thenReturn(details2);

        Collection<CustomerDetails> result = customerService.findAll();

        assertThat(result).containsExactlyInAnyOrder(details1, details2);
    }

    @Test
    void whenUpdate_withExistingId_shouldReturnUpdatedCustomerDetails() {
        UUID customerId = UUID.randomUUID();
        CustomerDetails customerDetailsToUpdate = CustomerDetails.builder()
                .firstName("John")

                .familyName("DoeUpdated")
                .email("john.doe.updated@example.com")
                .phoneNumber("1122334455")
                .licenceNumber("12012A123456")
                .birthDate(defaultBirthDate)
                .build();
        Customer existingCustomer = Customer.builder()
                .customerId(CustomerId.generate())
                .firstName("John")
                .familyName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address(defaultAddress)
                .licenceNumber("12012A123456")
                .birthDate(defaultBirthDate)
                .build();

        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepositoryPort.save(any(Customer.class))).thenReturn(existingCustomer);
        when(customerMapper.toCustomerDetails(existingCustomer)).thenReturn(customerDetailsToUpdate);

        Optional<CustomerDetails> result = customerService.update(customerDetailsToUpdate, customerId);

        verify(customerRepositoryPort).save(existingCustomer);
        assertThat(existingCustomer.getFamilyName()).isEqualTo("DoeUpdated");
        assertThat(result).isPresent().contains(customerDetailsToUpdate);
    }

    @Test
    void whenUpdate_withNonExistingId_shouldThrowException() {
        UUID customerId = UUID.randomUUID();
        CustomerDetails customerDetailsToUpdate = CustomerDetails.builder()
                .firstName("John")
                .familyName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .licenceNumber("12012A123456")
                .birthDate(defaultBirthDate)
                .build();
        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.update(customerDetailsToUpdate, customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void whenDelete_withExistingId_shouldDeleteCustomer() {
        UUID customerId = UUID.randomUUID();
        when(customerRepositoryPort.existsById(customerId)).thenReturn(true);

        customerService.delete(customerId);

        verify(customerRepositoryPort).deleteById(customerId);
    }

    @Test
    void whenDelete_withNonExistingId_shouldThrowException() {
        UUID customerId = UUID.randomUUID();
        when(customerRepositoryPort.existsById(customerId)).thenReturn(false);

        assertThatThrownBy(() -> customerService.delete(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }

}