package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.PermissionSavingDto;
import com.pashonokk.dvdrental.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionSavingMapper {
    Permission toEntity(PermissionSavingDto permissionSavingDto);
    PermissionSavingDto toDto(Permission permission);
}
