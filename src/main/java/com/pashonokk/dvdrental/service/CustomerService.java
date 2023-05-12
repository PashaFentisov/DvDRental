package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomer(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public void addCustomer(CustomerDto customerDto) {
        customerRepository.save(customerMapper.toEntity(customerDto));
    }
}
