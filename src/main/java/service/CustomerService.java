package service;

import entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public void addCustomer(Customer category) {
        customerRepository.save(category);
    }
}
