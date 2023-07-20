package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FilmSavingDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate releaseYear;
    private Duration rentalDuration;
    private Double rentalRate;
    private Duration length;
    private Double replacementCost;
    private Double rating;
    private LocalDate lastUpdate;
    private Set<Long> categoryIds;
}
