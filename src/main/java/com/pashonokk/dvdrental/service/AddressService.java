package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.exception.CustomerNotFoundException;
import com.pashonokk.dvdrental.mapper.AddressMapper;
import com.pashonokk.dvdrental.mapper.AddressSavingMapper;
import com.pashonokk.dvdrental.repository.AddressRepository;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final AddressSavingMapper addressSavingMapper;
    private final AddressMapper addressMapper;
    private final Logger log = LoggerFactory.getLogger(AddressService.class);


    @Transactional
    public AddressDto addAddressToCustomer(AddressSavingDto addressSavingDto) {
        Customer customer = customerRepository.findByIdWithAddress(addressSavingDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + addressSavingDto.getCustomerId() + " doesn`t exist"));
        if (customer.getAddress() != null) {
            log.warn("Customer {} already has address", customer);
            return null;
        }
        Address address = addressSavingMapper.toEntity(addressSavingDto);
        customer.addAddress(address);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Transactional
    public void deleteCustomersAddress(Long id) {
        customerRepository.findByIdWithAddress(id)
                .ifPresent(customer -> customer.removeAddress(customer.getAddress()));
    }

    @Transactional(readOnly = true)
    public AddressDto getAddressByCustomerId(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " doesn`t exist"));
        return addressMapper.toDto(address);
    }

    @Transactional
    public AddressDto updateAddress(AddressDto addressDto) {
        Address address = addressRepository.findById(addressDto.getId()).orElse(null);
        if (address == null) {
            log.warn("There isn`t address with id {}", addressDto.getId());
            return null;
        }
        Address updatedAddress = addressRepository.save(addressMapper.toEntity(addressDto));
        return addressMapper.toDto(updatedAddress);
    }

    @Transactional
    public AddressDto updateSomeFieldsOfAddress(AddressDto addressDto) {
        Address address = addressRepository.findById(addressDto.getId()).orElse(null);
        if (address == null) {
            return null;
        }
        Optional.ofNullable(addressDto.getDistrict()).ifPresent(address::setDistrict);
        Optional.ofNullable(addressDto.getPhone()).ifPresent(address::setPhone);
        Optional.ofNullable(addressDto.getLastUpdate()).ifPresent(address::setLastUpdate);
        Optional.ofNullable(addressDto.getStreet()).ifPresent(address::setStreet);
        if (addressDto.getHouseNumber() != 0) {
            address.setHouseNumber(addressDto.getHouseNumber());
        }
        if (addressDto.getPostalCode() != 0) {
            address.setPostalCode(addressDto.getPostalCode());
        }
        return addressMapper.toDto(address);
    }
}
