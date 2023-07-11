package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CitySavingDto {
    private Long id;
    private String name;
    private LocalDate lastUpdate;
    private Long countryId;
}
