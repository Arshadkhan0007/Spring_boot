package com.example.SecurityServiceApplication.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int userId;
    private String username;
    private String password;
    private List<Role> roleList;

}
