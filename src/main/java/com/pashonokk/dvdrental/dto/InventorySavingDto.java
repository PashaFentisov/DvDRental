package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventorySavingDto {
    private Long id;
    private Long storeId;
    private Long filmId;
    private LocalDate lastUpdate;
}
