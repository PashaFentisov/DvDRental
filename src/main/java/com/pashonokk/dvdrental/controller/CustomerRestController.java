package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


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

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerSavingDto customerSavingDto) {
        CustomerDto savedCustomer = customerService.addCustomer(customerSavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        customerDto.setId(id);
        CustomerDto updatedCustomerDto = customerService.partialUpdateCustomer(customerDto);
        return ResponseEntity.ok(updatedCustomerDto);
    }
}
