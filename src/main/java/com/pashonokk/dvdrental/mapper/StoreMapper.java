package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.StoreDto;
import com.pashonokk.dvdrental.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface StoreMapper {
    @Mapping(source = "addressDto", target = "address")
    Store toEntity(StoreDto storeDto);
    @Mapping(source = "address", target = "addressDto")
    StoreDto toDto(Store store);
}
