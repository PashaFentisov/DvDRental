package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CityDto;
import com.pashonokk.dvdrental.entity.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City toEntity(CityDto cityDto);
    CityDto toDto(City city);
}
