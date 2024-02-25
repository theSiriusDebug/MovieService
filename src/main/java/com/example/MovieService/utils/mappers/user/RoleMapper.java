package com.example.MovieService.utils.mappers.user;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.dtos.userDtos.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public static RoleDto mapToRoleDto(Role role){
        return new  RoleDto(
                role.getId(),
                role.getName()
        );
    }

    public static Role mapToRole(RoleDto roleDto){
        return new Role(
                roleDto.getId(),
                roleDto.getName()
        );
    }
}
