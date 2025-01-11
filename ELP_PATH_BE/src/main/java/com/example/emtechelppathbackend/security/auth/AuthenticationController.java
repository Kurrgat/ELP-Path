package com.example.emtechelppathbackend.security.auth;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {

        try {
            if (userRepository.existsByUserEmail(registerRequest.getUserEmail())) {
                throw new UserDetailsNotFoundException("User with this email already exists");
            }

            return ResponseEntity.ok(service.register(registerRequest));
        }
        catch (NoResourceFoundException e){
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequestAdmin requestAdmin){
        if (userRepository.existsByUserEmail(requestAdmin.getUserEmail())) {
            throw new UserDetailsNotFoundException("User with this email already exists");
        }

        return ResponseEntity.ok(service.registerAdmin(requestAdmin));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest,HttpServletRequest request) {

        if (!userRepository.existsByUserEmail(authenticationRequest.getUserEmail())) {
            throw new UserDetailsNotFoundException("Invalid Authentication Credentials");
        } else {
            Optional<Users> userToLog = userRepository.findUsersByUserEmail(authenticationRequest.getUserEmail());
            Users user = userToLog.get();
            if (!passwordEncoder.matches(authenticationRequest.getUserPassword(), user.getPassword())) {
                throw new UserDetailsNotFoundException("Invalid Authentication Credentials");
            }
            return ResponseEntity.ok(service.authenticate(authenticationRequest,  request));

        }
    }

    @PostMapping("/confirm/scholar-email")
    public ResponseEntity<?> verifyScholarEmail(@RequestBody ScholarAuthDto scholarAuthDto,  HttpServletRequest request) {
        var result = service.verifyScholarEmail(scholarAuthDto, request);

        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
