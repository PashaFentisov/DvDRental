package com.pashonokk.dvdrental.dto;

import com.pashonokk.dvdrental.entity.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {
    private Long id;
    private String name;
    private LocalDate lastUpdate;
    private Set<City> cities;
}
