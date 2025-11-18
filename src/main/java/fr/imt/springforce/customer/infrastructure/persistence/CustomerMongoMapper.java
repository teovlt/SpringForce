package fr.imt.springforce.customer.infrastructure.persistence;

import fr.imt.springforce.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMongoMapper {

    @Mapping(source = "publicId", target = "customerId.id")
    Customer toDomain(CustomerDocument document);

    @Mapping(source = "customerId.id", target = "publicId")
    CustomerDocument toDocument(Customer customer);

}
