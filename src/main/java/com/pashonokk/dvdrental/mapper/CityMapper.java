package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CountryMapper.class)
public interface CityMapper {
    @Mapping(source = "countryDto", target = "country")
    City toEntity(CityDto cityDto);
    @Mapping(source = "country", target = "countryDto")
    CityDto toDto(City city);
}
