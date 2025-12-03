package fr.imt.springforce.customer.infrastructure.persistence;

import fr.imt.springforce.customer.domain.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "customers")
@Getter
@Setter
public class CustomerDocument {

    @Id
    private String id;

    private UUID publicId;

    private String firstName;

    private String familyName;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String licenceNumber;

    private java.time.Instant birthDate;

    private Address address;

}
