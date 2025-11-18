package fr.imt.springforce.customer.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class CustomerId {

    UUID id;

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID());
    }

}
