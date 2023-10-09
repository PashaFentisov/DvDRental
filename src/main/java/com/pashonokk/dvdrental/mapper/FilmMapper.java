package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.entity.Film;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, LanguageMapper.class})
public interface FilmMapper {
    Film toEntity(FilmDto filmDto);

    FilmDto toDto(Film film);
}
