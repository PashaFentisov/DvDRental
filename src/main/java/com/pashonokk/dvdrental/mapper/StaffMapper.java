package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.entity.Staff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StoreMapper.class, AddressMapper.class, ContactInfoMapper.class})
public interface StaffMapper {
    Staff toEntity(StaffDto staffDto);
    StaffDto toDto(Staff staff);
}
