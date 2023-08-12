package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.time.Duration;
import java.time.OffsetDateTime;
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
    private OffsetDateTime releaseYear;
    private Duration rentalDuration;
    private Double rentalRate;
    private Duration length;
    private Double replacementCost;
    private Double rating;
    private OffsetDateTime lastUpdate;
    private Set<Long> categoriesIds;
    private Set<Long> languagesIds;
    private Set<Long> actorsIds;
}
