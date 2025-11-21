package com.example.SecurityServiceApplication.Repository;

import com.example.SecurityServiceApplication.Entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoleRepository {

    private final List<Role>roleList = List.of(
            new Role(1, "ROLE_ADMIN"),
            new Role(2, "ROLE_USER")
    );

    public Optional<Role> findByRoleId(int id) {
        return roleList
                .stream()
                .filter(role -> role.getRoleId() == id)
                .findFirst();
    }

}
