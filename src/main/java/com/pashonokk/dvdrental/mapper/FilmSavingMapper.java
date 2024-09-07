package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.FilmSavingDto;
import com.pashonokk.dvdrental.entity.Film;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilmSavingMapper {
    Film toEntity(FilmSavingDto filmDto);
    FilmSavingDto toDto(Film film);
}
