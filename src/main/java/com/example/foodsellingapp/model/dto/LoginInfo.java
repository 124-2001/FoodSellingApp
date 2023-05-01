package com.example.foodsellingapp.model.dto;

import com.example.foodsellingapp.model.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class LoginInfo {
    private Long userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private List<Role> roleName;
}
