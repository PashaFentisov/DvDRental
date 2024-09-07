package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CitySavingDto;
import com.pashonokk.dvdrental.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitySavingMapper {
    @Mapping(target = "countryId", ignore = true)
    CitySavingDto toDto(City city);

    @Mapping(target = "country", ignore = true)
    City toEntity(CitySavingDto citySavingDto);
}
