package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.InventoryDto;
import com.pashonokk.dvdrental.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FilmMapper.class, StoreMapper.class})
public interface InventoryMapper {
    @Mapping(source = "store", target = "store")
    @Mapping(source = "film", target = "film")
    Inventory toEntity(InventoryDto inventoryDto);

    @Mapping(source = "store", target = "store")
    @Mapping(source = "film", target = "film")
    InventoryDto toDto(Inventory inventory);
}
