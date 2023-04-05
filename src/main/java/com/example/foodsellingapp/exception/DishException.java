package com.example.foodsellingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DishException extends RuntimeException{
    public DishException(String message){
        super(String.format(message));
    }
}
