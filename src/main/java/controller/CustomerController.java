package controller;

import entity.Customer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import service.CustomerService;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping
    public Iterable<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return new RedirectView("getAll");
    }

    @PostMapping
    public RedirectView addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return new RedirectView("getAll");
    }

    @PutMapping("/{id}")
    public RedirectView updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
        customer.setId(id);
        customerService.addCustomer(customer);
        return new RedirectView("getAll");
    }
}
