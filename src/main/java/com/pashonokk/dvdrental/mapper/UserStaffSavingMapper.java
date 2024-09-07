package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.UserStaffSavingDto;
import com.pashonokk.dvdrental.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ContactInfoMapper.class)
public interface UserStaffSavingMapper {
    User toEntity(UserStaffSavingDto userStaffSavingDto);
    UserStaffSavingDto toDto(User user);
}
