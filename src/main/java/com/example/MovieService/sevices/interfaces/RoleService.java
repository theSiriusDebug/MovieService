package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.dtos.userDtos.RoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> findAllRoles();

    void saveRole(RoleDto role);

    Role findRoleByName(String roleName);
}
