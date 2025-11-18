package fr.imt.springforce.customer.infrastructure.web;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDetails save(@Valid @RequestBody CustomerDetails request) {
        return customerService.save(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetails findById(@PathVariable UUID id) {
        return customerService.findById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CustomerDetails> findAll() {
        return customerService.findAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetails update(@PathVariable UUID id, @Valid @RequestBody CustomerDetails request) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        customerService.delete(id);
    }

}
