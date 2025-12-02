package fr.imt.springforce.customer.application.mapper;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "customerId.id", target = "id")
    @Mapping(source = "licenceNumber", target = "licenceNumber")
    CustomerDetails toCustomerDetails(Customer customer);

    @Mapping(source = "id", target = "customerId.id")
    @Mapping(source = "licenceNumber", target = "licenceNumber")
    Customer toCustomer(CustomerDetails customerDetails);

}
