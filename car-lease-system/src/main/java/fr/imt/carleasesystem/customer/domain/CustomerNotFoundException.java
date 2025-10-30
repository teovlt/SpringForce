/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.carleasesystem.customer.domain;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
        super();
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

}
