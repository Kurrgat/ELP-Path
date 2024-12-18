package com.example.emtechelppathbackend.utils;

import com.example.emtechelppathbackend.security.user.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
    String message;
    Integer statusCode = HttpStatus.OK.value();
    Boolean success = true;
    T payload;

}
