package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.dto.PhoneDto;
import com.pashonokk.dvdrental.dto.PhoneSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import com.pashonokk.dvdrental.entity.Phone;
import com.pashonokk.dvdrental.mapper.AddressMapper;
import com.pashonokk.dvdrental.mapper.PhoneMapper;
import com.pashonokk.dvdrental.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private static final String ADDRESS_ERROR_MESSAGE = "Address with id %s doesn't exist";


    @Transactional(readOnly = true)
    public AddressDto getAddressWithCityAndCountryById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADDRESS_ERROR_MESSAGE, id)));
        return addressMapper.toDto(address);
    }

    @Transactional
    public AddressDto updateSomeFieldsOfAddress(AddressDto addressDto) {
        Address address = addressRepository.findById(addressDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADDRESS_ERROR_MESSAGE, addressDto.getId())));
        Optional.ofNullable(addressDto.getDistrict()).ifPresent(address::setDistrict);
        Optional.ofNullable(addressDto.getStreet()).ifPresent(address::setStreet);
        if (addressDto.getHouseNumber() != 0) {
            address.setHouseNumber(addressDto.getHouseNumber());
        }
        return addressMapper.toDto(address);
    }

    @Transactional
    public PhoneDto addPhoneToAddress(Long id, PhoneSavingDto phoneSavingDto) {
        Phone phone = Phone.builder()
                .isDeleted(false)
                .isMain(false)
                .number(phoneSavingDto.getNumber())
                .build();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ADDRESS_ERROR_MESSAGE, id)));
        phone.addAddress(address);
        return phoneMapper.toDto(phone);
    }
}
