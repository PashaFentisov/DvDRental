package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Long id;
    private String name;
    private LocalDate lastUpdate;
    private CountryDto countryDto;

    public CityDto(String name, LocalDate lastUpdate) {
        this.name = name;
        this.lastUpdate = lastUpdate;
    }
}
