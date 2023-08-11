package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.exception.CustomerWithoutAddressException;
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
        Address address = customer.getAddress();
        if(address==null){
            throw new CustomerWithoutAddressException("Store cannot exist without an address");
        }
        customer.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(customerSavingDto.getAddressSavingDto().getCityId()).ifPresent(city->city.addAddress(address));
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        customer.removeRentals(customer.getRentals());
        customer.removePayments(customer.getPayments());
        customerRepository.delete(customer);
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
