package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.StoreSavingDto;

import java.time.OffsetDateTime;

public class StoreBuilder {
    public static StoreSavingDto constructStore() {
        return StoreSavingDto.builder()
                .addressSavingDto(UserBuilder.constructAddress())
                .lastUpdate(OffsetDateTime.now())
                .isDeleted(false)
                .build();
    }

}
