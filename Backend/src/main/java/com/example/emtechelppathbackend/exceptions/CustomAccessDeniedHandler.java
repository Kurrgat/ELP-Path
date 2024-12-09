package com.example.emtechelppathbackend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

//Handles exceptions thrown when a user tries to access a resource
//that they're not authorized to access
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exc) throws IOException {
        response.addHeader("access_denied_reason", "not_authorized");
        response.sendError(403, "Access Denied");
    }
}
