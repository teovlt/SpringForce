package fr.imt.springForce.customer.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class Customer {

    CustomerId customerId;

    String firstName;

    String familyName;

    String email;

    String phoneNumber;

}
