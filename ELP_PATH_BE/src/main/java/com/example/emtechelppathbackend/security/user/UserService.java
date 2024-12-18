package com.example.emtechelppathbackend.security.user;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.twilio.rest.bulkexports.v1.export.ExportCustomJob;
import com.twilio.rest.preview.hostedNumbers.HostedNumberOrder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public interface UserService  {
    UsersDto getUserDetails(Long userId,UsersDto usersDto);
    CustomResponse<List<UsersDto>> viewAllUsers();
    public UsersDto mapUserToDto(Users users);
    Users mapUserDtoToEntity(UsersDto usersDto);
    CustomResponse<?> getTotalUsers();
    Users findUsersByEmail(String emailAddress) throws NoResourceFoundException;

    Users findUserByResetToken(String resetToken) throws NoResourceFoundException;

    Role updateUserRole(Long userId, String roleName);
    CustomResponse<List<UsersDto>> viewAdmins();
    CustomResponse<List<UsersDto>> viewUsersByRoleName(String roleName);
    void deleteUserById(Long userId) throws NoResourceFoundException;
    CustomResponse<?> countUsersByRoleName(String roleName);


    CustomResponse<?> addUser(UsersDto1 usersDto, Long roleId);

    CustomResponse<?>updateUser(Long userId,UsersDto1 updatedUserDto);

    CustomResponse<?>countUsers();

    CustomResponse<?> getUserById(Long userId);
}
