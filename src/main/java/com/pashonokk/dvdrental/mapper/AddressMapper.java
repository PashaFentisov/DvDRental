package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.AddressDto;
import com.pashonokk.dvdrental.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CityMapper.class)
public interface AddressMapper {
    @Mapping(source = "cityDto", target = "city")
    Address toEntity(AddressDto addressDto);
    @Mapping(source = "city", target = "cityDto")
    AddressDto toDto(Address address);
}
