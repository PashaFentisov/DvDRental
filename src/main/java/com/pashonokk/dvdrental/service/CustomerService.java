package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.entity.Phone;
import com.pashonokk.dvdrental.mapper.AddressSavingMapper;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CustomerMapper customerMapper;
    private final AddressSavingMapper addressSavingMapper;
    private final PageMapper pageMapper;
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
    public Customer constructCustomer(AddressSavingDto addressSavingDto, BigDecimal balance) {
        Phone phone = Phone.builder()
                .number(addressSavingDto.getNumber())
                .isMain(true)
                .isDeleted(false)
                .build();
        Customer customer = Customer.builder()
                .balance(balance)
                .createDate(OffsetDateTime.now())
                .lastUpdate(OffsetDateTime.now())
                .isDeleted(false)
                .build();
        Address address = addressSavingMapper.toEntity(addressSavingDto);
        address.setLastUpdate(OffsetDateTime.now());
        address.setIsDeleted(false);
        customer.addAddress(address);
        phone.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(addressSavingDto.getCityId()).ifPresent(city -> city.addAddress(address));
        return customer;
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        customer.setIsDeleted(true);
        customer.getAddress().setIsDeleted(true);
    }
}
