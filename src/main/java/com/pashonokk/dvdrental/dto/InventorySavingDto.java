package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventorySavingDto {
    private Long id;
    private Long storeId;
    private Long filmId;
    private OffsetDateTime lastUpdate;
}
