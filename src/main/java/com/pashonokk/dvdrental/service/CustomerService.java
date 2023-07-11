package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.exception.CustomerNotFoundException;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findByIdWithAddress(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " doesn`t exist "));
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
        Customer customer = customerMapper.toEntity(customerDto);
        try {
            customer.addAddress(customerDto.getAddress());
        } catch (Exception e) {
            log.warn("Add customer without address");
        }
        customerRepository.save(customer);
    }

    @Transactional
    public void partialUpdateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findByIdWithAddress(customerDto.getId()).orElse(null);
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
