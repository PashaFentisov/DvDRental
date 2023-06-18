package com.pashonokk.dvdrental.dto;

import com.pashonokk.dvdrental.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Long id;
    private String city;
    private LocalDate lastUpdate;
    private Country country;
}
