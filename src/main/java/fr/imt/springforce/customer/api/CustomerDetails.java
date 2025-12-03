package fr.imt.springforce.customer.api;

import fr.imt.springforce.customer.domain.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CustomerDetails {

    UUID id;

    @NotBlank
    String firstName;

    @NotBlank
    String familyName;

    @Email
    @NotBlank
    String email;

    @NotBlank
    String phoneNumber;

    Address address;

}
