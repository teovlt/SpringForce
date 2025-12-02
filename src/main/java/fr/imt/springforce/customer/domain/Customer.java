package fr.imt.springforce.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class Customer {

    public static Customer generate(
            String firstName,
            String familyName,
            String email,
            String phoneNumber) {
        return Customer.builder()
                .customerId(CustomerId.generate())
                .firstName(firstName)
                .familyName(familyName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

    private CustomerId customerId;

    private String firstName;

    private String familyName;

    private String email;

    private String phoneNumber;

    private String licenceNumber;

    private Instant birthDate;

    private Address address;

}
