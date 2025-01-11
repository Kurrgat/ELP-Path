package com.example.emtechelppathbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserDetailsNotFoundException extends RuntimeException{
    private String message;

    public UserDetailsNotFoundException(String message){
        super(message);
        this.message = message;
    }
}
