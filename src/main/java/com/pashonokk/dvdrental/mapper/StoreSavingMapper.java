package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.StoreSavingDto;
import com.pashonokk.dvdrental.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressSavingMapper.class)
public interface StoreSavingMapper {
    @Mapping(source = "addressSavingDto", target = "address")
    Store toEntity(StoreSavingDto storeSavingDto);
    @Mapping(source = "address", target = "addressSavingDto")
    StoreSavingDto toDto(Store store);
}
