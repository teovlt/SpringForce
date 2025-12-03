package fr.imt.springforce.customer.application;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.application.mapper.CustomerMapper;
import fr.imt.springforce.customer.domain.Address;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void setUp() {
        defaultAddress = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("75000")
                .city("Paris")
                .countryCode("FR")
                .build();

        defaultCustomerDetails = CustomerDetails.builder()
                .firstName("John")
                .familyName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address(defaultAddress)
                .licenceNumber("12012A123456")
                .build();


        defaultCustomer = Customer.generate(
                defaultCustomerDetails.getFirstName(),
                defaultCustomerDetails.getFamilyName(),
                defaultCustomerDetails.getEmail(),
                defaultCustomerDetails.getPhoneNumber(),
                defaultCustomerDetails.getAddress(),
                defaultCustomerDetails.getLicenceNumber()
        );
    }

    @Test
    void whenSavingNewCustomer_shouldReturnSavedCustomerDetails() {
        when(customerRepositoryPort.save(any(Customer.class))).thenReturn(defaultCustomer);
        when(customerMapper.toCustomerDetails(defaultCustomer)).thenReturn(defaultCustomerDetails);

        CustomerDetails result = customerService.save(defaultCustomerDetails);

        assertThat(result).isEqualTo(defaultCustomerDetails);
        verify(customerRepositoryPort).save(any(Customer.class));
    }

    @Test
    void whenFindById_withExistingId_shouldReturnCustomerDetails() {
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.generate("Jane", "Doe", "jane.doe@example.com", "0987654321", defaultAddress, "12012A123456");
        CustomerDetails expectedDetails = CustomerDetails.builder()
                .firstName("Jane")
                .familyName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("0987654321")
                .build();

        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerDetails(customer)).thenReturn(expectedDetails);

        CustomerDetails result = customerService.findById(customerId);

        assertThat(result).isEqualTo(expectedDetails);
    }

    @Test
    void whenFindById_withNonExistingId_shouldThrowException() {
        UUID customerId = UUID.randomUUID();
        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer not found with ID: " + customerId);
    }

    @Test
    void whenFindAll_shouldReturnAllCustomerDetails() {
        Customer customer1 = defaultCustomer;
        Customer customer2 = Customer.generate("Jane", "Smith", "jane.smith@example.com", "0987654321", defaultAddress, "12012A123456");
        List<Customer> customers = List.of(customer1, customer2);

        CustomerDetails details1 = defaultCustomerDetails;
        CustomerDetails details2 = CustomerDetails.builder()
                .firstName("Jane")
                .familyName("Smith")
                .email("jane.smith@example.com")
                .phoneNumber("0987654321")
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
                .build();
        Customer existingCustomer = Customer.generate("John", "Doe", "john.doe@example.com", "1234567890", defaultAddress, "12012A123456");

        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepositoryPort.save(any(Customer.class))).thenReturn(existingCustomer);
        when(customerMapper.toCustomerDetails(existingCustomer)).thenReturn(customerDetailsToUpdate); // Mock the mapper for the return value

        CustomerDetails result = customerService.update(customerId, customerDetailsToUpdate);

        verify(customerRepositoryPort).save(existingCustomer);
        assertThat(existingCustomer.getFamilyName()).isEqualTo("DoeUpdated");
        assertThat(result).isEqualTo(customerDetailsToUpdate);
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
                .build();
        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.update(customerId, customerDetailsToUpdate))
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

