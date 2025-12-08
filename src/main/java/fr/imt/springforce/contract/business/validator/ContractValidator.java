/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springforce.contract.business.validator;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.infrastructure.ContractRepository;
import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import fr.imt.springforce.vehicle.api.VehicleState;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ContractValidator implements Validator<ContractDetails> {

    private final VehicleClient vehicleClient;
    private final ContractRepository contractRepository;

    @Override
    public void validate(ContractDetails contract, ValidationResult result) {
        validateVehicleState(contract, result);
        validateDates(contract, result);
        validateOverlapping(contract, result);
    }

    private void validateVehicleState(ContractDetails contract, ValidationResult result) {
        if (vehicleClient.findById(contract.getVehicleId()).map(VehicleDetails::getState).orElse(VehicleState.AVAILABLE) == VehicleState.OUT_OF_ORDER) {
            result.addError(String.format("The vehicle %s is currently out of order and unavailable for rental", contract.getVehicleId()));
        }
    }

    private void validateDates(ContractDetails contract, ValidationResult result) {
        if (contract.getStartDate().isAfter(contract.getEndDate())) {
            result.addError("Rental begin date must be before end date");
        }
    }

    private void validateOverlapping(ContractDetails contract, ValidationResult result) {
        List<Contract> overlapping = contractRepository.findOverlappingContracts(
                contract.getVehicleId(),
                contract.getStartDate(),
                contract.getEndDate()
        );

        if (!overlapping.isEmpty()) {
            result.addError("The current vehicle is currently rented for this period");
        }
    }


}
