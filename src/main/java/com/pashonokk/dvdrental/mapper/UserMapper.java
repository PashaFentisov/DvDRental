package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.UserDto;
import com.pashonokk.dvdrental.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}
