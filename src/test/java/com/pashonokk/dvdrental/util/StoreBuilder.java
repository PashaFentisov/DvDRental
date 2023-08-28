package com.pashonokk.dvdrental.util;

import com.pashonokk.dvdrental.dto.StoreSavingDto;

public class StoreBuilder {
    public static StoreSavingDto constructStoreSavingDto() {
        return StoreSavingDto.builder()
                .addressSavingDto(UserBuilder.constructAddress())
                .build();
    }

}
