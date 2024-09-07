package com.pashonokk.dvdrental.util;

import com.github.javafaker.Faker;
import com.pashonokk.dvdrental.dto.FilmSavingDto;
import com.pashonokk.dvdrental.dto.LanguageDto;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Random;

public class FilmBuilder {
    private final static Random RANDOM = new Random();
    private final static Faker FAKER = new Faker(Locale.ENGLISH);


    public static FilmSavingDto constructFilm() {
        return FilmSavingDto.builder()
                .description(FAKER.lorem().paragraph())
                .isDeleted(false)
                .rating(RANDOM.nextDouble(10))
                .rentalRate(RANDOM.nextDouble(10))
                .releaseYear(OffsetDateTime.now().withYear(RANDOM.nextInt(1950, OffsetDateTime.now().getYear())))
                .replacementCost(RANDOM.nextDouble(50, 200))
                .title(FAKER.book().title())
                .length(Duration.ofMinutes(RANDOM.nextLong(60, 250)))
                .isDeleted(false)
                .lastUpdate(OffsetDateTime.now())
                .build();
    }

    public static LanguageDto constructLanguage() {
        return LanguageDto.builder()
                .name((FAKER.funnyName().name()))
                .lastUpdate(OffsetDateTime.now())
                .isDeleted(false)
                .build();

    }
}
