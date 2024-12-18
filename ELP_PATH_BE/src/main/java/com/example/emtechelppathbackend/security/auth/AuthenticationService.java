package com.example.emtechelppathbackend.security.auth;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.auditing.AuditingService;
import com.example.emtechelppathbackend.auditing.AuditingServiceImpl;
import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import com.example.emtechelppathbackend.security.jwtservices.JwtService;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.security.roles.RoleDto;
import com.example.emtechelppathbackend.security.roles.RoleRepository;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UsersRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    private final ScholarRepo scholarRepo;

    //inject the jwt service to handle token generation
    private final JwtService jwtService;

    private final EmailService emailService;

    // inject the password encoder service
    private final PasswordEncoder passwordEncoder;

    //injecting authentication manager
    private final AuthenticationManager authenticationManager;

    private  final AuditingServiceImpl auditingService;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest, HttpServletRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUserEmail(),
                        authenticationRequest.getUserPassword()
                )
        );

        var user = userRepository.findUsersByUserEmail(authenticationRequest.getUserEmail())
                .orElseThrow(() -> new UserDetailsNotFoundException("User not found"));

        // Record login attempt here
        auditingService.logLogin(request, user.getUserEmail());

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userEmail(authenticationRequest.getUserEmail())
                .role(modelMapper.map(user.getRole(), RoleDto.class))
                .token(jwtToken)
                .build();
    }


    public CustomResponse<?> verifyScholarEmail(ScholarAuthDto scholarAuthDto,  HttpServletRequest request) {
        CustomResponse<Object> response = new CustomResponse<>();

        try {
            if((scholarAuthDto.getScholarPfNo() != null && !scholarAuthDto.getScholarPfNo().isEmpty()) && (scholarAuthDto.getScholarEmail() != null && !scholarAuthDto.getScholarEmail().isEmpty())) {
                Optional<Scholar> scholarOptional = scholarRepo.findScholarByScholarCodePfNumber(scholarAuthDto.getScholarPfNo());

                if (scholarOptional.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("Invalid PF Number/Scholar Code");
                    response.setStatusCode(HttpStatus.FORBIDDEN.value());

                    return response;
                }

                //if we get to this point, we have a valid scholar
                Scholar scholar = scholarOptional.get();

                //CHECK IF THE SCHOLAR IS REGISTERED IN THE SYSTEM AS A USER
                Optional<Users> usersOptional = userRepository.findUserByScholarId(scholar.getId());
                if (usersOptional.isEmpty()){
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Valid initial value received. Please complete your registration.");
                    response.setSuccess(false);
                    return response;
                }

                if (usersOptional.get().getUserEmail() != null && !usersOptional.get().getUserEmail().isEmpty()) {
                    String userEmail = usersOptional.get().getUserEmail();

                    if(!userEmail.equals(scholarAuthDto.getScholarEmail())) {
                        response.setMessage("Scholar email does not match pf/scholar no provided");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    } else {
                        AuthenticationRequest authRequest = new AuthenticationRequest();
                        authRequest.setUserEmail(scholarAuthDto.getScholarEmail());
                        authRequest.setUserPassword(scholarAuthDto.getScholarPassword());
                        var result = authenticate(authRequest,request);

                        response.setMessage("Login Successful");
                        response.setPayload(result);
                    }
                }
            } else {
                response.setMessage("Pf No and Email must be provided");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }



    //creating users, saving them to the database and returning the generated token out of it
    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = Users.builder()
                .userEmail(registerRequest.getUserEmail())
                .firstName(registerRequest.getData().getFirstName())
                .lastName(registerRequest.getData().getLastName())
                .role(registerRequest.getRole())
                .userPassword(passwordEncoder.encode(registerRequest.getUserPassword()))
                .build();
        System.out.println(user);
        System.out.println(registerRequest.getData().getSearchResult());
        Users newUser = null;


        // Check if the role exists in the database //todo, change this to ensure all people are assigned to a role.
        Role role = user.getRole();


        if (role == null || role.getRoleName() == null) {

            System.out.println("role in the request is null or not specified");
            role = roleRepository.findByRoleName("ALUMNI");
            if (role == null) {
                // If the "ALUMNI" role does not exist, create it
//                role = new Role();
//                role.setRoleName("UNASSIGNED");
//                roleRepository.save(role);
                throw new NoResourceFoundException("create a role first");
            }
        } else {

            System.out.println("role in the request is" + role.getRoleName());
            Role existingRole = roleRepository.findByRoleName(role.getRoleName());
            if (existingRole == null) {
                throw new IllegalStateException("Specified role does not exist. Please create the role first.");
            }
            role = existingRole;
        }

        // Set the role for the Users entity
        user.setRole(role);

        //Connecting with Elp and Application Entities
        String whatWeHave = registerRequest.getData().getSearchResult();
        if (whatWeHave.equals("an unregistered scholar")) {
            //well, I had to do this to keep up with standards, but it should always have a WTF
            Optional<Scholar> possibleScholar = scholarRepo.findById(registerRequest.getData().getScholarId());
            Scholar scholar = possibleScholar.get();
            user.setScholar(scholar);
           //scholar.setUser(user);
            //scholarRepo.save(scholar);
           // newUser = scholar.getUser();
            scholar = user.getScholar();
            scholarRepo.save(scholar);
        }

        // Save the Users entity
        newUser.setRegisteringDate(LocalDateTime.now());
        System.out.println("the user details to save: " + newUser);
        userRepository.save(newUser);

        //notification
        EmailDetails emailDetails = emailService.SetRegistrationConfirmationBody(newUser);

        //in case you forgot, this is for a background task
        CompletableFuture.runAsync(() -> emailService.sendWithOutAttachment(emailDetails));


        //returning the authentication response that contains the token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .id(newUser.getId())
                .scholarId(registerRequest.getData().getScholarId())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .token(jwtToken)
                .userEmail(registerRequest.getUserEmail())
                .build();
    }

    public AuthenticationResponse registerAdmin(RegisterRequestAdmin requestAdmin) {
        var newAdmin = Users.builder()
                .firstName(requestAdmin.getFirstName())
                .lastName(requestAdmin.getLastName())
                .userEmail(requestAdmin.getUserEmail())
                .userPassword(passwordEncoder.encode(requestAdmin.getUserPassword()))
                .role(getRoleFromRequest(requestAdmin.getRole().getId()))
                .build();

        log.info("**** the role in the request is {}", newAdmin.getRole());
        newAdmin.setRegisteringDate(LocalDateTime.now());
        //notification
        EmailDetails emailDetails = emailService.SetRegistrationConfirmationBody(newAdmin);

        //in case you forgot, this is for a background task
        CompletableFuture.runAsync(() -> emailService.sendWithOutAttachment(emailDetails));
        userRepository.save(newAdmin);

        var token = jwtService.generateToken(newAdmin);

        return AuthenticationResponse.builder()
                .id(newAdmin.getId())
                .firstName(newAdmin.getFirstName())
                .lastName(newAdmin.getLastName())
                .userEmail(newAdmin.getUserEmail())
                .token(token)
                .build();
    }
    private Role getRoleFromRequest(Long roleId) throws NoResourceFoundException{
        Optional<Role> possibleRole = roleRepository.findById(roleId);
        if (possibleRole.isPresent()){
            return possibleRole.get();
        }
        else throw new NoResourceFoundException("Role of id " + roleId + " does not exist");
    }
}
