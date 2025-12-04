package fr.imt.springforce.contract.presentation;

import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.business.model.ContractState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Contract", description = "Contract API")
public class ContractController {

    private final ContractClient contractClient;

    @Operation(summary = "Create a new contract")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contract created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Contract creation failed")
    })
    @PostMapping
    public ResponseEntity<ContractDetails> createContract(@RequestBody ContractDetails contract) {
        try {
            ContractDetails created = contractClient.createContract(contract)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Contract creation failed"));
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get a contract by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the contract"),
            @ApiResponse(responseCode = "404", description = "Contract not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContractDetails> getContract(@PathVariable String id) {
        return contractClient.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all contracts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all contracts")
    })
    @GetMapping
    public ResponseEntity<List<ContractDetails>> getAllContracts() {
        return ResponseEntity.ok(contractClient.getAllContracts());
    }

    @Operation(summary = "Get contracts by client id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found contracts for the client")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContractDetails>> getContractsByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(contractClient.getContractsByClient(clientId));
    }

    @Operation(summary = "Get contracts by vehicle id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found contracts for the vehicle")
    })
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ContractDetails>> getContractsByVehicle(@PathVariable String vehicleId) {
        return ResponseEntity.ok(contractClient.getContractsByVehicle(vehicleId));
    }

    @Operation(summary = "Get contracts by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found contracts with the given status")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContractDetails>> getContractsByStatus(@PathVariable ContractState status) {
        return ResponseEntity.ok(contractClient.getContractsByStatus(status));
    }

    @Operation(summary = "Cancel a contract")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contract cancelled"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Contract not found"),
            @ApiResponse(responseCode = "500", description = "Contract cancellation failed")
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ContractDetails> cancelContract(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            String reason = body.getOrDefault("reason", "Annulation demandée");
            ContractDetails cancelled = contractClient.cancelContract(id, reason)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Contract cancellation failed"));
            return ResponseEntity.ok(cancelled);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // The completeContract method is internal to the service and not exposed via the client.
    // If it needs to be exposed, it should be added to the ContractClient interface and use DTOs.
}