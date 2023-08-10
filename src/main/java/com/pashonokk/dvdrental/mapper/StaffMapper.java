package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.StaffDto;
import com.pashonokk.dvdrental.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StoreMapper.class, AddressMapper.class})
public interface StaffMapper {
    @Mapping(source = "addressDto", target = "address")
    @Mapping(source = "storeDto", target = "store")
    Staff toEntity(StaffDto staffDto);
    @Mapping(source = "address", target = "addressDto")
    @Mapping(source = "store", target = "storeDto")
    StaffDto toDto(Staff staff);
}
