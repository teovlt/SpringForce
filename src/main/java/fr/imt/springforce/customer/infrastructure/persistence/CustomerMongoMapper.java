package fr.imt.springforce.customer.infrastructure.persistence;

import fr.imt.springforce.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMongoMapper {

    @Mapping(source = "publicId", target = "customerId.id")
    @Mapping(source = "licenceNumber", target = "licenceNumber")
    Customer toDomain(CustomerDocument document);

    @Mapping(source = "customerId.id", target = "publicId")
    @Mapping(source = "licenceNumber", target = "licenceNumber")
    CustomerDocument toDocument(Customer customer);

}
