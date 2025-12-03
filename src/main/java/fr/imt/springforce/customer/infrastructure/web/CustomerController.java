package fr.imt.springforce.customer.infrastructure.web;

import fr.imt.springforce.customer.api.CustomerClient;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
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
public class CustomerController {

    private final CustomerClient customerClient;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDetails save(@Valid @RequestBody CustomerDetails request) {
        return customerClient.save(request)
                .orElseThrow(() -> new RuntimeException("Customer creation failed"));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetails findById(@PathVariable UUID id) {
        return customerClient.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CustomerDetails> findAll() {
        return customerClient.findAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetails update(@PathVariable UUID id, @Valid @RequestBody CustomerDetails request) {
        return customerClient.update(request, id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        customerClient.delete(id);
    }

}
