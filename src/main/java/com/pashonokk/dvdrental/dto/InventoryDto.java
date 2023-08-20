package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private Long id;
    private StoreDto store;
    private FilmDto film;
    private OffsetDateTime lastUpdate;
    private Boolean isDeleted;
}
