package com.example.foodsellingapp.payload.response;

import com.example.foodsellingapp.model.dto.TokenInfo;
import lombok.Data;

@Data
public class LoginResponse {
    private TokenInfo token;
    private Long userId;
    private String firstName;
    private String lastName;
    private String fullName;
}
