package com.example.emtechelppathbackend.security.roles;

import com.example.emtechelppathbackend.security.permissions.Permissions;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDto {
    private Long id;
    private String roleName;
    private Set<Permissions> permissions;
}
