package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private Long id;
    private StoreDto store;
    private FilmDto film;
    private LocalDate lastUpdate;
}