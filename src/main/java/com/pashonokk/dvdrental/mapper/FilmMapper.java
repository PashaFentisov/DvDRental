package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.entity.Film;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, LanguageMapper.class})
public interface FilmMapper {
    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "languages", target = "languages")
    Film toEntity(FilmDto filmDto);

    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "languages", target = "languages")
    FilmDto toDto(Film film);
}
