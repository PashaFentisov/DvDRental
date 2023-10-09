package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.AddressSavingDto;
import com.pashonokk.dvdrental.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressSavingMapper {
    Address toEntity(AddressSavingDto addressSavingDto);
    AddressSavingDto toDto(Address address);
}
