package com.example.foodsellingapp.model.dto;

import com.example.foodsellingapp.model.eenum.RoleName;
import com.example.foodsellingapp.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTPayloadDto {
    private Long userId;
    private List<Role> roleNames;
}
