package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.StoreDto;
import com.pashonokk.dvdrental.entity.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface StoreMapper {
    Store toEntity(StoreDto storeDto);
    StoreDto toDto(Store store);
}
