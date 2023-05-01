package com.example.foodsellingapp.payload.request;

import lombok.Data;

import java.util.List;
@Data
public class SignupRequest {
    private String username;

    private String email;

//    private List<String> role;

    private String password;


}
