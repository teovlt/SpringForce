/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.carleasesystem.customer.infrastructure.persistence;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Value
@Document(collection = "customers")
public class CustomerDocument {

    @Id
    String id;

    UUID publicId;

    String firstName;

    String familyName;

    @Indexed(unique = true)
    String email;

    String phoneNumber;

}
