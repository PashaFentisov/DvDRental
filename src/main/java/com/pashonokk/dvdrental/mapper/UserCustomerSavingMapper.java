package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.UserCustomerSavingDto;
import com.pashonokk.dvdrental.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ContactInfoMapper.class)
public interface UserCustomerSavingMapper {
    User toEntity(UserCustomerSavingDto userDto);

    UserCustomerSavingDto toDto(User user);
}
