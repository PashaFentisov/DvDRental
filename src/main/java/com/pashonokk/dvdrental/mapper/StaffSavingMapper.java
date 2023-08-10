package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.StaffSavingDto;
import com.pashonokk.dvdrental.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressSavingMapper.class)
public interface StaffSavingMapper {
    @Mapping(source = "addressSavingDto", target = "address")
    Staff toEntity(StaffSavingDto staffDto);
    @Mapping(source = "address", target = "addressSavingDto")
    StaffSavingDto toDto(Staff staff);
}
