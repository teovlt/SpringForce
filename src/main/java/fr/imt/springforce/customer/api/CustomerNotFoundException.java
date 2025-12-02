package fr.imt.springforce.customer.api;

import fr.imt.springforce.common.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException(String customerId) {
        // Pass the specific message up to the base class
        super("Customer not found with ID: " + customerId);
    }

}
