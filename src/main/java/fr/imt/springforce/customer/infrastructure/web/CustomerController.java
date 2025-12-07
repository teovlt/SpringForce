package fr.imt.springforce.customer.infrastructure.web;

import fr.imt.springforce.customer.api.CustomerClient;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Customer", description = "Customer API")
public class CustomerController {

    private final CustomerClient customerClient;

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Customer creation failed")
    })
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDetails save(@Valid @RequestBody CustomerDetails request) {
        return customerClient.save(request)
                .orElseThrow(() -> new RuntimeException("Customer creation failed"));
    }

    @Operation(summary = "Get a customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetails findById(@PathVariable UUID id) {
        return customerClient.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
    }

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all customers")
    })
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CustomerDetails> findAll() {
        return customerClient.findAll();
    }

    @Operation(summary = "Update a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetails update(@PathVariable UUID id, @Valid @RequestBody CustomerDetails request) {
        return customerClient.update(request, id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
    }

    @Operation(summary = "Delete a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        customerClient.delete(id);
    }

}
