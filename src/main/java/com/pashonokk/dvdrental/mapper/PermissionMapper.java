package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.PermissionDto;
import com.pashonokk.dvdrental.entity.Permission;
import com.pashonokk.dvdrental.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionDto permissionDto);
    @Mapping(target = "roleNames", expression = "java(mapRolesToNames(permission.getRoles()))")
    PermissionDto toDto(Permission permission);

    default Set<String> mapRolesToNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
