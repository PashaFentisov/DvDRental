package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.PermissionDto;
import com.pashonokk.dvdrental.dto.PermissionSavingDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Permission;
import com.pashonokk.dvdrental.entity.Role;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.mapper.PermissionMapper;
import com.pashonokk.dvdrental.mapper.PermissionSavingMapper;
import com.pashonokk.dvdrental.repository.PermissionRepository;
import com.pashonokk.dvdrental.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionMapper permissionMapper;
    private final PermissionSavingMapper permissionSavingMapper;
    private final PageMapper pageMapper;
    private static final String PERMISSION_ERROR_MESSAGE = "Permission with id %s doesn't exist";


    @Transactional(readOnly = true)
    public PermissionDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PERMISSION_ERROR_MESSAGE, id)));
        return permissionMapper.toDto(permission);
    }

    @Transactional(readOnly = true)
    public PageResponse<PermissionDto> getAllPermissions(Pageable pageable) {
        return pageMapper.toPageResponse(permissionRepository.findAll(pageable).map(permissionMapper::toDto));
    }

    @Transactional
    public PermissionDto addPermission(PermissionSavingDto permissionSavingDto) {
        Permission permission = permissionSavingMapper.toEntity(permissionSavingDto);
        List<Role> rolesById = roleRepository.findAllByIdAndPermissions(permissionSavingDto.getRoleIds());
        permission.addRoles(rolesById);
        permission.setIsDeleted(false);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDto(savedPermission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PERMISSION_ERROR_MESSAGE, id)));
        permission.setIsDeleted(true);
    }
}
