package com.example.SecurityServiceApplication.Repository;

import com.example.SecurityServiceApplication.Entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    private RoleRepository roleRepo = new RoleRepository();

    private final List<User> userList = List.of(
            new User(101, "Alex@gmail.com", "alex123", List.of(roleRepo.findByRoleId(1).get(), roleRepo.findByRoleId(2).get())),
            new User(102, "Benny@gmail.com", "benny123", List.of(roleRepo.findByRoleId(2).get()))
    );

    public Optional<User> findByUsername(String username) {
        return userList
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

}
