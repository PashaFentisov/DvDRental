package com.pashonokk.dvdrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitySavingDto {
    private Long id;
    private String name;
    private LocalDate lastUpdate;
    private Long countryId;
}
