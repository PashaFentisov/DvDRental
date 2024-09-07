package com.pashonokk.dvdrental.mapper;


import com.pashonokk.dvdrental.dto.RentalDto;
import com.pashonokk.dvdrental.entity.Rental;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, StaffMapper.class, InventoryMapper.class})
public interface RentalMapper {
    Rental toEntity(RentalDto rentalDto);
    RentalDto toDto(Rental rental);
}
