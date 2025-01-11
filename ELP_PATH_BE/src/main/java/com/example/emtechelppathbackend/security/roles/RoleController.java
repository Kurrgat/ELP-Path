package com.example.emtechelppathbackend.security.roles;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;
import com.example.emtechelppathbackend.responserecords.ResponseRecordOFMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleService;

    @GetMapping("/all")
    public ResponseEntity<?> getRoles() {
        var response = roleService.displayRoles();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{roleId}/view")
    public ResponseEntity<?> displayRole(@PathVariable Long roleId) {
        var response = roleService.displayRoleById(roleId);
        return  ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @PostMapping("/add-new-role")
    public ResponseEntity<?> addRole(@RequestBody Role role) {
        try {
            Role newRole = roleService.addNewRole(role);
            return new ResponseEntity<>(new ResponseRecord("new role added successfully", newRole), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseRecordOFMessages(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{roleId}/update")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId, @RequestBody Role updatedRole) {
        try {
            roleService.updateRoleById(roleId, updatedRole);
            return new ResponseEntity<>(new ResponseRecord("Role updated successfully", updatedRole), HttpStatus.OK);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{roleId}/delete")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long roleId) {
        try {
            boolean successDelete = roleService.deleteRole(roleId);
            if (successDelete) {
                return new ResponseEntity<>(new ResponseRecord("role deleted successfully", null), HttpStatus.OK);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseRecord("Role not found or couldn't be deleted", null));
            }
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }
}