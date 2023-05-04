package controller;

import entity.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import service.CustomerService;

import java.time.LocalDate;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/get")
    public Customer getCustomer(@RequestParam("id") Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping("/getAll")
    public Iterable<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/delete")
    public RedirectView deleteCustomer(@RequestParam("id") Long id) {
        customerService.deleteCustomer(id);
        return new RedirectView("getAll");
    }

    @GetMapping("/add")
    public RedirectView addCustomer(@RequestParam("firstname") String firstname,
                                    @RequestParam("lastname") String lastname,
                                    @RequestParam("email") String email) {
        Customer customer = new Customer(firstname, lastname, email, LocalDate.now(), LocalDate.now(), true);
        customerService.addCustomer(customer);
        return new RedirectView("getAll");
    }
}
