package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.InventorySavingDto;
import com.pashonokk.dvdrental.entity.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventorySavingMapper {

    Inventory toEntity(InventorySavingDto inventoryDto);

    InventorySavingDto toDto(Inventory inventory);
}
