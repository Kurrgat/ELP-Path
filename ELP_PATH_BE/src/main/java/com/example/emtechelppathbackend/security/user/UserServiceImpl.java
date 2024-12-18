package com.example.emtechelppathbackend.security.user;


import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;

import com.example.emtechelppathbackend.scholars.UserCountInterface;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.security.roles.RoleDto;
import com.example.emtechelppathbackend.security.roles.RoleRepository;

import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UsersRepository userRepository;
    private final RoleRepository roleRepository;

    private final EntityManager entityManager;



    @Override
    public UsersDto getUserDetails(Long userId, UsersDto usersDto) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("User not found"));
        return null;
    }

    @Override
    public CustomResponse<List<UsersDto>> viewAllUsers() {
        CustomResponse<List<UsersDto>>response=new CustomResponse<>();
        try {
            List<UsersDto> result = userRepository.findAll().stream()
                    .map(users -> modelMapper.map(users, UsersDto.class))
                    .collect(Collectors.toList());

            if (result.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No application found ");
                response.setPayload(null);}

            else {

                response.setPayload(result);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }



    //a count of all registered users
    @Override
    public CustomResponse<?> getTotalUsers() {
        CustomResponse<Long>response=new CustomResponse<>();
        try {
            String queryString = "SELECT COUNT(a) FROM Users a";
            TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
            var result= query.getSingleResult();
            if (result==null) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No user found ");
                response.setPayload(null);}

            else {

                response.setPayload(result);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }


    @Override
    public Users findUsersByEmail(String emailAddress) throws NoResourceFoundException {
        Optional<Users> possibleUsers = userRepository.findUsersByUserEmail(emailAddress);
        if (possibleUsers.isPresent()) {
            return possibleUsers.get();
        } else throw new NoResourceFoundException("No user is found with this email address");
    }

    @Override
    public Users findUserByResetToken(String resetToken) throws NoResourceFoundException {
        Optional<Users> possibleUser = userRepository.findUsersByPasswordResetToken(resetToken);
        return possibleUser.get();
    }

    @Override
    public Role updateUserRole(Long userId, String roleName) {
        //find user
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDetailsNotFoundException("User not found"));

        //retrieve user's current role
        //Role role = user.getRole();

        Role newRole = roleRepository.findByRoleName(roleName);

        //update role
        // role.setRoleName(roleName);

        //reassign role to user
        user.setRole(newRole);

        // Save the updated user
        userRepository.save(user);
        return newRole;
    }

    @Override
    public CustomResponse<List<UsersDto>> viewAdmins() {
        CustomResponse<List<UsersDto>>response=new CustomResponse<>();
        try {
            var results= userRepository.findUsersByRoleIsNotAlumni().stream()
                    .map(this::mapUserToDto).toList();
            if (results.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No application found ");
                response.setPayload(null);}

            else {

                response.setPayload(results);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<List<UsersDto>> viewUsersByRoleName(String roleName) {
        CustomResponse<List<UsersDto>>response=new CustomResponse<>();
        try {
            var results= userRepository.findUsersByRole_RoleName(roleName).stream()
                    .map(this::mapUserToDto).toList();
            if (results.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No Users found ");
                response.setPayload(null);}

            else {

                response.setPayload(results);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }


    @Override
    public void deleteUserById(Long userId) throws NoResourceFoundException {
        Optional<Users> userToDelete = userRepository.findById(userId);
        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
        } else {
            throw new NoResourceFoundException("Specified user is not found");
        }
    }

    @Override
    public CustomResponse<?> countUsersByRoleName(String roleName) {
        CustomResponse<Long>response=new CustomResponse<>();
        try {
            var results= userRepository.countUsersByRole_RoleName(roleName);
            if (results==null) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No users found ");
                response.setPayload(null);}

            else {

                response.setPayload(results);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }

    public CustomResponse<?> addUser(UsersDto1 usersDto, Long roleId) {
        CustomResponse<Users> response = new CustomResponse<>();

        try {
            String username = usersDto.getUsername();
            Optional<Users> existingUsername = userRepository.findUsersByUsername(username);
            if (existingUsername.isPresent()) {
                response.setMessage("The username already exists. Please choose a different username.");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Check if the email already exists
            String email = usersDto.getUserEmail();
            Optional<Users> existingEmail = userRepository.findUsersByUserEmail(email);
            System.out.println(existingEmail);
            if (existingEmail.isPresent()) {
                response.setMessage("The email already exists. Please choose a different email.");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Create a new Users entity based on the provided UsersDto
            Users newAdmin = new Users();
            newAdmin.setFirstName(usersDto.getFirstName());
            newAdmin.setLastName(usersDto.getLastName());
            newAdmin.setUserPassword(usersDto.getUserPassword());
            newAdmin.setUserEmail(usersDto.getUserEmail());
            newAdmin.setUsername(usersDto.getUsername());

            // Check if the role with the provided roleId exists
            Optional<Role> existingRole = roleRepository.findById(roleId);

            if (existingRole.isPresent()) {
                // Set the role name in the Users entity
                newAdmin.setRole(existingRole.get());
            } else {
                response.setMessage("Role with id " + roleId + " does not exist.");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Save the new admin user to the repository
            userRepository.save(newAdmin);

            response.setMessage(" user added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(newAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<?> updateUser(Long userId, UsersDto1 updatedUserDto) {
        CustomResponse<Users> response = new CustomResponse<>();

        try {
            // Step 1: Retrieve the existing user entity
            Optional<Users> optionalUser = userRepository.findById(userId);

            optionalUser.ifPresent(existingUser -> {
                // Update basic user properties
                existingUser.setFirstName(updatedUserDto.getFirstName());
                existingUser.setLastName(updatedUserDto.getLastName());
                existingUser.setUsername(updatedUserDto.getUsername());
                existingUser.setUserEmail(updatedUserDto.getUserEmail());
                existingUser.setUserPassword(updatedUserDto.getUserPassword());



                // Assuming you have a method in UsersDto1 to update the user data
                updatedUserDto.updateUserData(existingUser);

                // Step 3: Save the updated user entity back to the database
                Users savedUser = userRepository.save(existingUser);

                response.setMessage("User updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(savedUser);
            });

            if (optionalUser.isEmpty()) {
                response.setMessage("User not found with id: " + userId);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<?> countUsers() {
        CustomResponse<UserCountInterface>response=new CustomResponse<>();
        try {
            var results= userRepository.countUsers();
            if (results==null) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No users found ");
                response.setPayload(null);}

            else {

                response.setPayload(results);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(results);
            }

        } catch (Exception e) {

            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getUserById(Long userId) {
        CustomResponse<Users>response=new CustomResponse<>();
        try {
            Optional<Users>results= userRepository.getUserById(userId);


            if (results.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage(" User not found ");
                response.setPayload(null);}

            else {

                response.setPayload(results.get());
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
            }

        } catch (Exception e) {
            response.setMessage("Internal server error: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }


    public UsersDto mapUserToDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());
        usersDto.setUserEmail(users.getUserEmail());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        usersDto.setRole(modelMapper.map(users.getRole(), RoleDto.class));

        return usersDto;
    }

    public Users mapUserDtoToEntity(UsersDto usersDto) {
        Users users = new Users();
       // users.setId(usersDto.getId());
        users.setUserEmail(usersDto.getUserEmail());
        users.setFirstName(usersDto.getFirstName());
        users.setLastName(usersDto.getLastName());
        return users;
    }




}
