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
public class FilmDto {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime releaseYear;
    private Double rentalRate;
    private Duration length;
    private Double replacementCost;
    private Double rating;
    private OffsetDateTime lastUpdate;
    private Set<CategoryDto> categories;
    private Set<LanguageDto> languages;
    private Boolean isDeleted;
}
