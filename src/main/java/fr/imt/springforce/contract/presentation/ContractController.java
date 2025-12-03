package fr.imt.springforce.contract.presentation;

import fr.imt.springforce.contract.api.ContractClient;
import fr.imt.springforce.contract.api.ContractDetails;
import fr.imt.springforce.contract.business.model.ContractState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContractController {

    private final ContractClient contractClient;

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

    @GetMapping("/{id}")
    public ResponseEntity<ContractDetails> getContract(@PathVariable String id) {
        return contractClient.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ContractDetails>> getAllContracts() {
        return ResponseEntity.ok(contractClient.getAllContracts());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ContractDetails>> getContractsByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(contractClient.getContractsByClient(clientId));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ContractDetails>> getContractsByVehicle(@PathVariable String vehicleId) {
        return ResponseEntity.ok(contractClient.getContractsByVehicle(vehicleId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContractDetails>> getContractsByStatus(@PathVariable ContractState status) {
        return ResponseEntity.ok(contractClient.getContractsByStatus(status));
    }

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