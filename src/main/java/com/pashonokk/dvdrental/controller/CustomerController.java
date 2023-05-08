package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private final CustomerService customerService;

    public static final String REDIRECT_TO_ALL = "customers";


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping
    public Iterable<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PostMapping
    public RedirectView addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PutMapping("/{id}")
    public RedirectView updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        customerService.addCustomer(customer);
        return new RedirectView(REDIRECT_TO_ALL);
    }
}
