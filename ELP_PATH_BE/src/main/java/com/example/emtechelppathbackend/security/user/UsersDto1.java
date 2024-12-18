package com.example.emtechelppathbackend.security.user;

import com.example.emtechelppathbackend.security.roles.RoleDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class UsersDto1 {

    private  Long id;
    private String firstName;
    private String username;
    private String lastName;

    private String userEmail;

    private String userPassword;




    public void updateUserData(Users existingUser) {
    }
}
