package fr.imt.springforce.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class Customer {

    private CustomerId customerId;

    private String firstName;

    private String familyName;

    private String email;

    private String phoneNumber;

    private String licenceNumber;

    private Instant birthDate;

    private Address address;

}
