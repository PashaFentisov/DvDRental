package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.InventoryDto;
import com.pashonokk.dvdrental.entity.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FilmMapper.class, StoreMapper.class})
public interface InventoryMapper {

    Inventory toEntity(InventoryDto inventoryDto);

    InventoryDto toDto(Inventory inventory);
}
