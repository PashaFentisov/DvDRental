package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressSavingMapper {
    @Mapping(target = "customer", ignore = true)
    Address toEntity(AddressSavingDto addressSavingDto);
    @Mapping(target = "customerId", ignore = true)
    AddressSavingDto toDto(Address address);
}
