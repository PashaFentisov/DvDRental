package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.mapper.CustomerSavingMapper;
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
    private final CustomerSavingMapper customerSavingMapper;
    private final CityService cityService;
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private static final String ERROR_MESSAGE = "Customer with id %s doesn't exist";


    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findByIdWithFullAddress(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toDto);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdWithAddress(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        customer.removeAddress(customer.getAddress());
        customerRepository.deleteById(id);
    }

    @Transactional
    public CustomerDto addCustomer(CustomerSavingDto customerSavingDto) {
        Customer customer = customerSavingMapper.toEntity(customerSavingDto);
        Address address = customer.getAddress();
        try {
            customer.addAddress(address);
        } catch (Exception e) {
            log.warn("Add customer without address");
        }
        try {
            Long cityId = customerSavingDto.getAddressSavingDto().getCityId();
            if(cityId==null){
               throw new EntityNotFoundException("Address without city");
            }
            cityService.addAddressToCity(address, cityId);
        } catch (Exception e) {
            log.warn("Address without city");
        }
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional
    public CustomerDto partialUpdateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerDto.getId())
                        .orElseThrow(()-> new EntityNotFoundException(String.format(ERROR_MESSAGE, customerDto.getId())));
        Optional.ofNullable(customerDto.getFirstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(customerDto.getLastUpdate()).ifPresent(customer::setLastUpdate);
        Optional.ofNullable(customerDto.getCreateDate()).ifPresent(customer::setCreateDate);
        Optional.ofNullable(customerDto.getLastName()).ifPresent(customer::setLastName);
        Optional.ofNullable(customerDto.getEmail()).ifPresent(customer::setEmail);
        return customerMapper.toDto(customer);
    }
}
