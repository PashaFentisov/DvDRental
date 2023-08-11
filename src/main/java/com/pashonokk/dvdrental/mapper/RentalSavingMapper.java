package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.RentalSavingDto;
import com.pashonokk.dvdrental.entity.Rental;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RentalSavingMapper {
    Rental toEntity(RentalSavingDto rentalSavingDto);
    RentalSavingDto toDto(Rental rental);
}
