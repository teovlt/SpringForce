package fr.imt.springforce.contract.api;

import fr.imt.springforce.common.exception.ResourceNotFoundException;

public class ContractNotFoundException extends ResourceNotFoundException {

    public ContractNotFoundException(String contractId) {
        // Pass the specific message up to the base class
        super("Contract not found with ID: " + contractId);
    }

}
