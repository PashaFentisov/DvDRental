package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

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


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
