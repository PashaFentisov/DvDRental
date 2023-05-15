package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toDto);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public void addCustomer(CustomerDto customerDto) {
        customerRepository.save(customerMapper.toEntity(customerDto));
    }

    @Transactional
    public void partialUpdateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerDto.getId()).orElse(null);
        if (customer == null) {
            return;
        }
        if (customerDto.getFirstName() != null) {
            customer.setFirstName(customerDto.getFirstName());
        }
        if (customerDto.getLastName() != null) {
            customer.setLastName(customerDto.getLastName());
        }
        if (customerDto.getEmail() != null) {
            customer.setEmail(customerDto.getEmail());
        }
        if (customerDto.getLastUpdate() != null) {
            customer.setLastUpdate(customerDto.getLastUpdate());
        }
        if (customerDto.getCreateDate() != null) {
            customer.setCreateDate(customerDto.getCreateDate());
        }
    }
}
