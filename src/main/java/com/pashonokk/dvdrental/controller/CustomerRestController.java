package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerBalanceDto;
import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    private final Logger logger = LoggerFactory.getLogger(CustomerRestController.class);


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerDto>> getCustomers(@RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                                  @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 customers at one time");
        }
        PageResponse<CustomerDto> allCustomers = customerService.getAllCustomers(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allCustomers);
    }

    @PostMapping("/balance/top-up")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).TOP_UP_CUSTOMER_BALANCE_ACCESS)")
    public ResponseEntity<CustomerDto> topUpCustomerBalance(@RequestBody @Valid CustomerBalanceDto customerBalanceDto,
                                                            Errors errors) {
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(er -> logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        CustomerDto customerWithNewBalance = customerService.topUpCustomerBalance(customerBalanceDto);
        return ResponseEntity.ok(customerWithNewBalance);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
