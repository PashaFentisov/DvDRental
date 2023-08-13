package com.pashonokk.dvdrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.annotation.ValidOffsetDateTime;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Title can`t be empty or null")
    private String title;
    @NotBlank(message = "Description can`t be empty or null")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @ValidOffsetDateTime
    private OffsetDateTime releaseYear;
    private Duration rentalDuration;
    private Double rentalRate;
    private Duration length;
    private Double replacementCost;
    private Double rating;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
    private Set<Long> categoriesIds;
    @NotNull
    @NotEmpty(message = "The list of languagesIds must not be empty")
    private Set<Long> languagesIds;
    private Set<Long> actorsIds;
}
