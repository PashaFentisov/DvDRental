package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CityMapper.class, PhoneMapper.class})
public interface AddressMapper {
    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
}
