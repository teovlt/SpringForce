/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springForce.customer.infrastructure.persistence;

import fr.imt.springForce.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMongoMapper {

    @Mapping(source = "publicId", target = "customerId.id")
    Customer toDomain(CustomerDocument document);

    @Mapping(source = "customerId.id", target = "publicId")
    CustomerDocument toDocument(Customer customer);

}
