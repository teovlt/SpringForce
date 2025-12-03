package fr.imt.springforce.customer.domain;

import fr.imt.springforce.customer.api.CustomerDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "customerId.id", target = "id")
    CustomerDetails toCustomerDetails(Customer customer);

    @Mapping(source = "id", target = "customerId.id")
    Customer toCustomer(CustomerDetails customerDetails);

}
