/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springForce.common;

public abstract class ResourceNotFoundException extends RuntimeException {

    protected ResourceNotFoundException(String message) {
        super(message);
    }

}
