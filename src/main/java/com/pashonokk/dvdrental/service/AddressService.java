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

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final AddressSavingMapper addressSavingMapper;
    private final AddressMapper addressMapper;
    private final Logger log = LoggerFactory.getLogger(AddressService.class);


    @Transactional
    public void addAddressToCustomer(AddressSavingDto addressSavingDto) {
        Customer customer = customerRepository.findByIdWithAddress(addressSavingDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + addressSavingDto.getCustomerId() + " doesn`t exist"));
        if (customer.getAddress() != null) {
            log.warn("Customer {} already has address", customer);
            return;
        }
        Address address = addressSavingMapper.toEntity(addressSavingDto);
        customer.addAddress(address);
    }

    @Transactional
    public void deleteCustomersAddress(Long id) {
        Customer customer = customerRepository.findByIdWithAddress(id).orElse(null);
        if (customer == null) {
            return;
        }
        customer.removeAddress(customer.getAddress());
    }

    @Transactional(readOnly = true)
    public AddressDto getAddressByCustomerId(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " doesn`t exist"));
        return addressMapper.toDto(address);
    }

    @Transactional
    public void updateAddress(AddressDto addressDto) {
        Address address = addressRepository.findById(addressDto.getId()).orElse(null);
        if (address == null) {
            log.warn("There isn`t address with id {}", addressDto.getId());
            return;
        }
        addressRepository.save(addressMapper.toEntity(addressDto));
    }

    @Transactional
    public void updateSomeFieldsOfAddress(AddressDto addressDto) {
        Address address = addressRepository.findById(addressDto.getId()).orElse(null);
        if (address == null) {
            return;
        }
        if (addressDto.getHouseNumber() != 0) {
            address.setHouseNumber(addressDto.getHouseNumber());
        }
        if (addressDto.getPostalCode() != 0) {
            address.setPostalCode(addressDto.getPostalCode());
        }
        if (addressDto.getDistrict() != null) {
            address.setDistrict(addressDto.getDistrict());
        }
        if (addressDto.getPhone() != null) {
            address.setPhone(addressDto.getPhone());
        }
        if (addressDto.getStreet() != null) {
            address.setStreet(addressDto.getStreet());
        }
        if (addressDto.getLastUpdate() != null) {
            address.setLastUpdate(addressDto.getLastUpdate());
        }
    }
}
