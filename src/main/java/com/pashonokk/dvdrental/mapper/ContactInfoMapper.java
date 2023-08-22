package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.ContactInfoDto;
import com.pashonokk.dvdrental.entity.ContactInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactInfoMapper {
    ContactInfo toEntity(ContactInfoDto contactInfoDto);
    ContactInfoDto toDto(ContactInfo customer);
}
