package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.mapper.AddressMapper;
import com.pashonokk.dvdrental.mapper.AddressSavingMapper;
import com.pashonokk.dvdrental.repository.AddressRepository;
import com.pashonokk.dvdrental.repository.CityRepository;
import com.pashonokk.dvdrental.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final CityRepository cityRepository;
    private final AddressSavingMapper addressSavingMapper;
    private final AddressMapper addressMapper;
    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private static final String ADDRESS_ERROR_MESSAGE = "Address with id %s doesn't exist";
    private static final String CUSTOMER_ERROR_MESSAGE = "Customer with id %s doesn't exist";



    @Transactional
    public AddressDto addAddressToCustomer(AddressSavingDto addressSavingDto) {
        Customer customer = customerRepository.findByIdWithAddress(addressSavingDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE, addressSavingDto.getCustomerId())));
        if (customer.getAddress() != null) {
            log.warn("Customer {} already has address", customer);
            return null;
        }
        Address address = addressSavingMapper.toEntity(addressSavingDto);
        customer.addAddress(address);
        cityRepository.findByIdWithAddressesAndCountry(addressSavingDto.getCityId()).ifPresent(city->city.addAddress(address));
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Transactional(readOnly = true)
    public AddressDto getAddressWithCityAndCountryByCustomerId(Long id) {
        Address address = addressRepository.findByIdAndCityAndCountry(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CUSTOMER_ERROR_MESSAGE, id)));
        return addressMapper.toDto(address);
    }

    @Transactional
    public void deleteCustomersAddress(Long id) {
        customerRepository.findByIdWithAddress(id)
                .ifPresent(customer -> customer.removeAddress(customer.getAddress()));
    }

    @Transactional
    public AddressDto updateSomeFieldsOfAddress(AddressDto addressDto) {
        Address address = addressRepository.findById(addressDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADDRESS_ERROR_MESSAGE, addressDto.getId())));
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
