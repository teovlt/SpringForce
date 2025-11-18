/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.carleasesystem.customer.domain;

import fr.imt.carleasesystem.customer.api.CustomerDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "customerId.id", target = "id")
    CustomerDetails toCustomerDetails(Customer customer);

    @Mapping(source = "id", target = "customerId.id")
    Customer toCustomer(CustomerDetails customerDetails);

}
