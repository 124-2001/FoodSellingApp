package com.example.foodsellingapp.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class SignupRequest {
    @NotNull
    private String username;
    @NotNull
    private String email;

//    private List<String> role;
    @NotNull
    private String password;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private String address;
    @NotNull
    private String phone;


}
