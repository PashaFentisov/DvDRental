package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CountryDto;
import com.pashonokk.dvdrental.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    Country toEntity(CountryDto countryDto);
    CountryDto toDto(Country country);
}
