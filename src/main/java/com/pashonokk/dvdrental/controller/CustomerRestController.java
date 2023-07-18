package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.dto.PageDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping(value = "/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getCustomers(@RequestBody PageDto pageDto) {
        if (pageDto.getSize() > 100) {
            throw new BigSizeException("You can get maximum 100 customers at one time");
        }
        Page<CustomerDto> allCustomers = customerService.getAllCustomers(PageRequest.of(pageDto.getPage(), pageDto.getSize(), Sort.by(pageDto.getSort())));
        return ResponseEntity.ok(allCustomers);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerSavingDto customerSavingDto) {
        CustomerDto savedCustomerDto = customerService.addCustomer(customerSavingDto);
        return ResponseEntity.created(URI.create("localhost:10000/customers/" + savedCustomerDto.getId())).body(savedCustomerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDto> updateSomeFieldsOfCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        customerDto.setId(id);
        CustomerDto updatedCustomerDto = customerService.partialUpdateCustomer(customerDto);
        return ResponseEntity.ok(updatedCustomerDto);
    }
}
