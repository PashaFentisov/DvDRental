package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.InventorySavingDto;

import java.time.OffsetDateTime;

public class InventoryBuilder {
    public static InventorySavingDto constructInventorySavingDto(Long filmId, Long storeId) {
        return InventorySavingDto.builder()
                .filmId(filmId)
                .storeId(storeId)
                .lastUpdate(OffsetDateTime.now())
                .isDeleted(false)
                .build();
    }
}
