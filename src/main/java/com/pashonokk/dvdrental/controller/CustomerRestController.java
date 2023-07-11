package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping(value = "/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    public static final String REDIRECT_TO_ALL = "/customers";

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping
    public Page<CustomerDto> getCustomers(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size,
                                          @RequestParam(required = false, defaultValue = "firstName") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
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

    @PatchMapping("/{id}")
    public RedirectView updateSomeFieldsOfCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        customerDto.setId(id);
        customerService.partialUpdateCustomer(customerDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }
}
