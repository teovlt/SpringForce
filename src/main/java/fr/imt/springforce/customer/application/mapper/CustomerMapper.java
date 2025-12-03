package fr.imt.springforce.customer.application.mapper;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "customerId.id", target = "id")
    CustomerDetails toCustomerDetails(Customer customer);

    @Mapping(target = "customerId", ignore = true)
    Customer toCustomer(CustomerDetails customerDetails);

    @Mapping(target = "customerId", ignore = true)
    void updateCustomerFromDetails(CustomerDetails customerDetails, @MappingTarget Customer customer);

}
