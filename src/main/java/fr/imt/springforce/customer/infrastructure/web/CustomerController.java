package fr.imt.springforce.customer.infrastructure.web;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerDetails> save(@Valid @RequestBody CustomerDetails request) {
        return new ResponseEntity<>(customerService.save(request), HttpStatus.CREATED);
    }

}
