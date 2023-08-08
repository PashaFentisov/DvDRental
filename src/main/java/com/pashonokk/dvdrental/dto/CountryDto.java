package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {
    private Long id;
    private String name;
    private LocalDate lastUpdate;

    public CountryDto(String name, LocalDate lastUpdate) {
        this.name = name;
        this.lastUpdate = lastUpdate;
    }
}
