package fr.imt.springforce.customer.infrastructure.persistence;

import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Value
@Document(collection = "customers")
@Setter
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
