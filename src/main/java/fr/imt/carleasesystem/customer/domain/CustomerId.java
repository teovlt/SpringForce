/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.carleasesystem.customer.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class CustomerId {

    UUID id;

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID());
    }

}
