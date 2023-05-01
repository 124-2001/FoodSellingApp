package com.example.foodsellingapp.payload.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String idToken;
    private String accessToken;
    private String refreshToken;
}
