package com.example.MovieService.sevices;

import com.example.MovieService.models.Role;
import com.example.MovieService.models.dtos.userDtos.RoleDto;
import com.example.MovieService.repositories.RoleRepository;
import com.example.MovieService.sevices.interfaces.RoleService;
import com.example.MovieService.utils.mappers.user.RoleMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RoleDto> findAllRoles() {
        log.info("Retrieving all roles from the database");
        return repository.findAll().stream()
                .map(RoleMapper::mapToRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveRole(@Valid RoleDto role) {
        log.info("Saving role: {}", role.getName());
        repository.save(Objects.requireNonNull(RoleMapper.mapToRole(role), "Role cannot be null."));
    }

    @Override
    public Role findRoleByName(@NotBlank String roleName) {
        log.info("Searching for role with name: {}", roleName);
        return Objects.requireNonNull(repository.findByName(roleName));
    }
}
