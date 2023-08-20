package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.mapper.CustomerSavingMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CustomerMapper customerMapper;
    private final PageMapper pageMapper;
    private final CustomerSavingMapper customerSavingMapper;
    private static final String ERROR_MESSAGE = "Customer with id %s doesn't exist";


    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findCustomerById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
    }

    @Transactional(readOnly = true)
    public PageResponse<CustomerDto> getAllCustomers(Pageable pageable) {
        return pageMapper.toPageResponse(customerRepository.findAll(pageable).map(customerMapper::toDto));
    }

    @Transactional
    public CustomerDto addCustomer(CustomerSavingDto customerSavingDto) {
        Customer customer = customerSavingMapper.toEntity(customerSavingDto);
        customer.setLastUpdate(OffsetDateTime.now());
        Address address = customer.getAddress();
        address.setLastUpdate(OffsetDateTime.now());
        customer.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(customerSavingDto.getAddress().getCityId()).ifPresent(city->city.addAddress(address));
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        customer.setIsDeleted(true);
        customer.getAddress().setIsDeleted(true);
    }

    @Transactional
    public CustomerDto partialUpdateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerDto.getId())
                        .orElseThrow(()-> new EntityNotFoundException(String.format(ERROR_MESSAGE, customerDto.getId())));
        Optional.ofNullable(customerDto.getContactInfo().getFirstName()).ifPresent(customer.getContactInfo()::setFirstName);
        Optional.ofNullable(customerDto.getCreateDate()).ifPresent(customer::setCreateDate);
        Optional.ofNullable(customerDto.getContactInfo().getLastName()).ifPresent(customer.getContactInfo()::setLastName);
        Optional.ofNullable(customerDto.getContactInfo().getEmail()).ifPresent(customer.getContactInfo()::setEmail);
        return customerMapper.toDto(customer);
    }
}
