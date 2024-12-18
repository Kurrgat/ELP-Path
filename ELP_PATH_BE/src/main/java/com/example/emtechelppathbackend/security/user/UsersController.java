package com.example.emtechelppathbackend.security.user;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/view_all")
    public ResponseEntity<CustomResponse<List<UsersDto>>> viewAllUsers() {
        var response= userService.viewAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/view-admins")
    public ResponseEntity<?> displayAdmins() {
        var response = userService.viewAdmins();
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/view-admins-with-role/{roleName}")
    public ResponseEntity<?> displayAdminsByRole(@PathVariable String roleName) {
        var response = userService.viewUsersByRoleName(roleName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/count-users-with-role/{roleName}")
    public ResponseEntity<?> countByRole(@PathVariable String roleName){
        var response= userService.countUsersByRoleName(roleName);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/count-users/")
    public ResponseEntity<?> countUsers(){
        var response= userService.countUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/count-all-users")
    public ResponseEntity<?> countAllUsers() {
        var response= userService.getTotalUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-user-role/{userId}/{roleName}")
    public Role updateUserRole(@PathVariable Long userId, @PathVariable String roleName) {
        return userService.updateUserRole(userId, roleName);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok(new ResponseRecord("deleted successfully", null));
        } catch (NoResourceFoundException e) {
            return ResponseEntity.ok(new ResponseRecord(e.getMessage(), null));
        }
    }
    @PostMapping("/create/user/{roleId}")
    public ResponseEntity<?> addUser(@RequestBody UsersDto1 usersDto, @PathVariable Long roleId) {

        var response = userService.addUser(usersDto, roleId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UsersDto1 updatedUserDto,@PathVariable Long userId) {
        var response= userService.updateUser(userId, updatedUserDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/get-user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        var response= userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




}
