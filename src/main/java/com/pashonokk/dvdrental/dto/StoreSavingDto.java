package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreSavingDto {
    private Long id;
    private AddressSavingDto addressSavingDto;
    private OffsetDateTime lastUpdate;
}
