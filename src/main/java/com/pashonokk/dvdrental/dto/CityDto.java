package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Long id;
    private String name;
    private OffsetDateTime lastUpdate;
    private CountryDto country;
    private Boolean isDeleted;

    public CityDto(String name, OffsetDateTime lastUpdate) {
        this.name = name;
        this.lastUpdate = lastUpdate;
    }
}
