package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " doesn`t exist "));
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
    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        try {
            customer.addAddress(customerDto.getAddress());
        } catch (Exception e) {
            log.warn("Add customer without address");
        }
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional
    public CustomerDto partialUpdateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findByIdWithAddress(customerDto.getId())
                        .orElseThrow(()-> new EntityNotFoundException("Customer with id " + customerDto.getId() + " doesn`t exist"));
        Optional.ofNullable(customerDto.getFirstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(customerDto.getLastUpdate()).ifPresent(customer::setLastUpdate);
        Optional.ofNullable(customerDto.getCreateDate()).ifPresent(customer::setCreateDate);
        Optional.ofNullable(customerDto.getLastName()).ifPresent(customer::setLastName);
        Optional.ofNullable(customerDto.getEmail()).ifPresent(customer::setEmail);
        return customerMapper.toDto(customer);
    }
}
