/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springforce.customer.domain;

import lombok.Builder;
import lombok.Value;

/**
 *  Represents an ISO 19160-1:2015 and ISO 3166-1 compliant postal address
 */
@Value
@Builder
public class Address {

    String streetName;
    String streetNumber;
    String buildingName;
    String unitNumber;
    String postalCode;
    String city;
    String stateProvince;
    String countryCode;
    String countryName;
    
}
