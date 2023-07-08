package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.exception.CustomerNotFoundException;
import com.pashonokk.dvdrental.mapper.AddressMapper;
import com.pashonokk.dvdrental.mapper.AddressSavingMapper;
import com.pashonokk.dvdrental.repository.AddressRepository;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final AddressSavingMapper addressSavingMapper;
    private final AddressMapper addressMapper;

    @Transactional
    public void addAddressToCustomer(AddressSavingDto addressSavingDto) {
        Customer customer = customerRepository.findById(addressSavingDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + addressSavingDto.getCustomerId() + " doesn`t exist"));
        Address address = addressSavingMapper.toEntity(addressSavingDto);
        address.setCustomer(customer);
        addressRepository.save(address);
    }
}
