package com.pashonokk.dvdrental.mapper;


import com.pashonokk.dvdrental.dto.PhoneDto;
import com.pashonokk.dvdrental.entity.Phone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper {
    Phone toEntity(PhoneDto phoneDto);

    PhoneDto toDto(Phone phone);
}
