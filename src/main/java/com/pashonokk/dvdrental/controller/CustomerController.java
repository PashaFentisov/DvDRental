package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.service.CustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private final CustomerService customerService;

    public static final String REDIRECT_TO_ALL = "customers";


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping
    public List<CustomerDto> getCustomers(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size,
                                          @RequestParam(required = false, defaultValue = "firstName") String sort) {
        return customerService.getAllCustomers(PageRequest.of(page, size, Sort.by(sort)));
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PostMapping
    public RedirectView addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.addCustomer(customerDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PutMapping("/{id}")
    public RedirectView updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        customerDto.setId(id);
        customerService.addCustomer(customerDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }
}
