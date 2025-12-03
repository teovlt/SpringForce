package fr.imt.springforce.contract.presentation;

import fr.imt.springforce.contract.business.model.Contract;
import fr.imt.springforce.contract.business.model.ContractState;
import fr.imt.springforce.contract.business.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContractController {

    private final ContractService contractService;

    /**
     * Créer un nouveau contrat
     */
    @PostMapping
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        try {
            Contract created = contractService.createContract(contract);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupérer un contrat par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable String id) {
        try {
            return ResponseEntity.ok(contractService.getContractById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupérer tous les contrats
     */
    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    /**
     * Récupérer les contrats d'un client
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Contract>> getContractsByClient(@PathVariable String clientId) {
        return ResponseEntity.ok(contractService.getContractsByClient(clientId));
    }

    /**
     * Récupérer les contrats d'un véhicule
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Contract>> getContractsByVehicle(@PathVariable String vehicleId) {
        return ResponseEntity.ok(contractService.getContractsByVehicle(vehicleId));
    }

    /**
     * Récupérer les contrats par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Contract>> getContractsByStatus(@PathVariable ContractState status) {
        return ResponseEntity.ok(contractService.getContractsByStatus(status));
    }

    /**
     * Annuler un contrat
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Contract> cancelContract(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            String reason = body.getOrDefault("reason", "Annulation demandée");
            return ResponseEntity.ok(contractService.cancelContract(id, reason));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Terminer un contrat (retour du véhicule)
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Contract> completeContract(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        try {
            LocalDateTime returnDate = body.containsKey("returnDate")
                    ? LocalDateTime.parse(body.get("returnDate"))
                    : LocalDateTime.now();
            return ResponseEntity.ok(contractService.completeContract(id, returnDate));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}