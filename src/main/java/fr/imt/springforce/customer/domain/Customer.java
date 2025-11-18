package fr.imt.springforce.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Value
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

    CustomerId customerId;

    String firstName;

    String familyName;

    String email;

    String phoneNumber;


}
