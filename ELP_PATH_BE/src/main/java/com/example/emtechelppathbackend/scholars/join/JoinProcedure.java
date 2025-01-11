package com.example.emtechelppathbackend.scholars.join;

import com.example.emtechelppathbackend.emails.EmailDetails;
import com.example.emtechelppathbackend.emails.EmailService;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import com.example.emtechelppathbackend.scholars.VerifyScholarRequest;
import com.example.emtechelppathbackend.security.auth.AuthenticationResponse;
import com.example.emtechelppathbackend.security.auth.AuthenticationService;
import com.example.emtechelppathbackend.security.jwtservices.JwtService;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.security.roles.RoleRepository;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.ScholarNameConfirm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinProcedure {

    private final ScholarRepo scholarRepository;
    private final UsersRepository usersRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String pfNumberOrscholarCode;
    //Step 1: Verify PF number/scholar code
    //Check that we have a scholar with the given PF number/scholar code
    //in our records
    public CustomResponse<?> authenticateUserInput(String pfNumberOrscholarCode){

        this.pfNumberOrscholarCode = pfNumberOrscholarCode;

        CustomResponse<Object> response = new CustomResponse<>();
        try{
            log.info("*****Received input: {}", pfNumberOrscholarCode);
            Optional<Scholar> scholarOptional = scholarRepository.findScholarByScholarCodePfNumber(pfNumberOrscholarCode);

            if (scholarOptional.isEmpty()){
                response.setSuccess(false);
                response.setMessage("Invalid PF Number/Scholar Code");
                response.setStatusCode(HttpStatus.FORBIDDEN.value());

                return response;
            }

            //if we get to this point, we have a valid scholar
            Scholar scholar = scholarOptional.get();

            //CHECK IF THE SCHOLAR IS REGISTERED IN THE SYSTEM AS A USER
            Optional<Users> usersOptional = usersRepository.findUserByScholarId(scholar.getId());

            //if scholar is not registered on the system as a user
            if (usersOptional.isEmpty()){
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Valid initial value received. Please complete your registration.");
                response.setSuccess(false);
                response.setPayload(scholar);
                return response;
            }

            //If the scholar exists on the system as a registered user
            //Authenticate the user
            Map<String, String > resp = new HashMap<>();
            resp.put("userEmail",usersOptional.get().getUserEmail());
            resp.put("pfNumber",scholar.getPfNumber());
            resp.put("scholarCode",scholar.getScholarCode());
            resp.put("username", usersOptional.get().getUsername());

            response.setPayload(resp);
            response.setMessage("Login");

        } catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }

        return response;
    }

    //Step 2 - Verify scholar
    //Scholar provides information to prove that they're who they claim to be
    public CustomResponse<?> verifyScholar(VerifyScholarRequest request){
        CustomResponse<Object> response = new CustomResponse<>();

        try {
            Optional<Scholar> possibleScholar = scholarRepository.findScholarByScholarCodePfNumber(request.pfNumberOrscholarCode());

            if(possibleScholar.isEmpty()) {
                return null;
            }

            Scholar scholar = possibleScholar.get();

            log.info("***********Scholar first name: {} ****Scholar last name:: {} ", scholar.getScholarFirstName(), scholar.getScholarLastName());
            log.info("**********Input first name: {} **********Input last name: {}", request.firstName(), request.lastName());


            log.info("***********Scholar high school id: {}", scholar.getSchool().getId());
            log.info("**********Input high school id: {}", request.highSchoolId());

            log.info("***********Scholar branch id: {}", scholar.getBranch().getId());
            log.info("**********Input branch id: {}", request.homeBranchId());

            String fullName = scholar.getScholarFirstName()+" "+scholar.getScholarLastName();


            boolean nameCorrect = ScholarNameConfirm.checkName(fullName, request.firstName(), request.middleName(), request.lastName());

            //check if the scholar names are correct
            if (!nameCorrect) {
                response.setSuccess(false);
                response.setMessage("Incorrect name provided");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            if (!(scholar.getSchool().getId().equals(request.highSchoolId()))) {
                response.setSuccess(false);
                response.setMessage("Invalid high school name");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }


            if (!scholar.getBranch().getId().equals(request.homeBranchId())) {
                response.setSuccess(false);
                response.setMessage("Invalid home branch name");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            //if we get to this point, all details provided by the scholar match
            //with the records we have
            response.setSuccess(true);
            response.setMessage("continue to registration");
            response.setStatusCode(HttpStatus.OK.value());

            return response;

        } catch (Exception e){
                response.setMessage("Something went wrong");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());

                return response;
            }
    }

    //Step 3 -> Register scholar
    public CustomResponse<?> register(RegisterAsUserRequest request){
        CustomResponse<Object> response = new CustomResponse<>();

        System.out.println("Register Request: " +request);

        try{
            if (Objects.isNull(request)){
                response.setSuccess(false);
                response.setMessage("No data provided");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            if (Objects.isNull(request.pfNumberOrScholarCode())){
                response.setSuccess(false);
                response.setMessage("PF Number/Scholar Code is required");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            if (Objects.isNull(request.email()) || !isValidEmail(request.email())){
                response.setSuccess(false);
                response.setMessage("Valid email is required");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            if (Objects.isNull(request.password())){
                response.setSuccess(false);
                response.setMessage("Password is required");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            if (Objects.isNull(request.username())){
                response.setSuccess(false);
                response.setMessage("Username is required");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                return response;
            }

            //if user has not accepted the terms and conditions
            if(!request.acceptedTermsAndConditions()){
                response.setSuccess(false);
                response.setMessage("Accept terms and conditions before being allowed to register");
                response.setStatusCode(HttpStatus.FORBIDDEN.value());

                return response;
            }


            Optional<Scholar> scholarOptional = scholarRepository.findScholarByScholarCodePfNumber(request.pfNumberOrScholarCode());

            if (scholarOptional.isEmpty()){
                response.setSuccess(false);
                response.setMessage("Invalid PF Number/Scholar Code");
                response.setStatusCode(HttpStatus.FORBIDDEN.value());

                return response;
            }

            Scholar scholar = scholarOptional.get();

            //check if the scholar is already a registered user of our system
            Optional<Users> scholarExists = usersRepository.findUserByScholarId(scholar.getId());
            if (scholarExists.isPresent()){
               //If the user is already registered, log them in
                response = (CustomResponse<Object>) authenticateUserInput(request.pfNumberOrScholarCode());
                return response;
            }

            if (usersRepository.existsByUserEmail(request.email())){
                response.setSuccess(false);
                response.setMessage("The email provided already exists.");
                response.setStatusCode(HttpStatus.FORBIDDEN.value());

                return response;
            }


            var user = Users.builder()
                    .userEmail(request.email())
                    .firstName(scholar.getScholarFirstName())
                    .lastName(scholar.getScholarLastName())
                    .role(null)
                    .userPassword(passwordEncoder.encode(request.password()))
                    .username(request.username())
                    .scholar(scholar)
                    .acceptedTermsAndConditions(request.acceptedTermsAndConditions())
                    .build();

            Role role = user.getRole();

            if (Objects.isNull(roleRepository.findByRoleName("ALUMNI"))) {
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("No role available");
                response.setSuccess(false);
                return response;
            }


            // Set the role for the Users entity
            user.setRole(roleRepository.findByRoleName("ALUMNI"));


            usersRepository.save(user);

            //notification
            EmailDetails emailDetails = emailService.SetRegistrationConfirmationBody(user);

            CompletableFuture.runAsync(() -> emailService.sendWithOutAttachment(emailDetails));


            //returning the authentication response that contains the token
            var jwtToken = jwtService.generateToken(user);

            var auth = AuthenticationResponse.builder()
                    .id(user.getId())
                    .scholarId(scholar.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .token(jwtToken)
                    .userEmail(user.getUserEmail())
                    .build();

            response.setSuccess(true);
            response.setMessage("User registered successfully");
            response.setPayload(auth);
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }
}