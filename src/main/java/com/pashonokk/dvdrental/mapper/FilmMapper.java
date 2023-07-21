package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.entity.Film;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface FilmMapper {
    @Mapping(source = "categories", target = "categories")
    Film toEntity(FilmDto filmDto);
    @Mapping(source = "categories", target = "categories")
    FilmDto toDto(Film film);
}
