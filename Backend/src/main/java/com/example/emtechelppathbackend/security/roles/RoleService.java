package com.example.emtechelppathbackend.security.roles;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface RoleService {
   void createSuperAdminRoleAndUser();
   Role addNewRole(Role role);

   CustomResponse<List<RoleDto>> displayRoles();

   void updateRoleById(Long roleId, Role updatedRole);

   CustomResponse<Role> displayRoleById(Long roleId);

   boolean deleteRole(Long roleId) throws NoResourceFoundException;
}