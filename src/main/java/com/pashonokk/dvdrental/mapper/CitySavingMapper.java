package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitySavingMapper {
    @Mapping(source = "lastUpdate", target = "lastUpdate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    CitySavingDto toDto(City city);

    @Mapping(source = "lastUpdate", target = "lastUpdate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    City toEntity(CitySavingDto citySavingDto);
}
