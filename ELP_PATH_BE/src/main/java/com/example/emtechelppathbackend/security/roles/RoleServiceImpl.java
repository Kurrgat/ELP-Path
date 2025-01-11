package com.example.emtechelppathbackend.security.roles;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.permissions.Permissions;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void createSuperAdminRoleAndUser() {
        Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN");
        if (superAdminRole == null) {
            // Create the SUPER_ADMINS role with all permissions
            superAdminRole = new Role();
            superAdminRole.setRoleName("SUPER_ADMIN");
            superAdminRole.setPermissions(EnumSet.allOf(Permissions.class));
            roleRepository.save(superAdminRole);


            // Create a new user with the SUPER_ADMINS role
            Users newUser = new Users();
            newUser.setFirstName("Super");
            newUser.setLastName("Admin");
            newUser.setUserEmail("admin@example.com");
            newUser.setUserPassword(passwordEncoder.encode("1234"));
            newUser.setRole(superAdminRole);
            usersRepository.save(newUser);
        } else {
            log.info("*#*&^%  System already has an admin");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAlumniRole() {
        // Check if "ALUMNI" role already exists
        Role alumniRole = roleRepository.findByRoleName("ALUMNI");
        if (alumniRole == null) {
            alumniRole = new Role();
            alumniRole.setRoleName("ALUMNI");
            alumniRole.setPermissions(new HashSet<>(Arrays.asList(
                    Permissions.SOCIALS_UPDATE, Permissions.SOCIALS_ADD, Permissions.SOCIALS_VIEW,
                    Permissions.PROFILE_UPDATE, Permissions.PROFILE_ADD, Permissions.PROFILE_VIEW,
                    Permissions.PROFILE_VIEW_ALL, Permissions.PROFILE_DELETE,
                    Permissions.FEEDS_UPDATE, Permissions.FEEDS_ADD, Permissions.FEEDS_VIEW,
                    Permissions.FEEDS_USER_VIEW, Permissions.FEEDS_VIEW_ALL, Permissions.FEEDS_DELETE,
                    Permissions.EVENTS_PARTICIPATE, Permissions.EVENTS_CANCEL_PARTICIPATION,
                    Permissions.EVENTS_VIEW, Permissions.EVENTS_DATE_VIEW, Permissions.EVENTS_DATE_COUNT,
                    Permissions.EVENTS_CHAPTER_VIEW, Permissions.EVENTS_CHAPTER_COUNT,
                    Permissions.EVENTS_ACTIVE_VIEW, Permissions.EVENTS_ACTIVE_COUNT,
                    Permissions.EVENTS_PAST_VIEW, Permissions.EVENTS_PAST_COUNT,
                    Permissions.EVENTS_SCHEDULED_VIEW, Permissions.EVENTS_SCHEDULED_COUNT,
                    Permissions.EVENTS_VIEW_ALL, Permissions.EVENTS_COUNT_ALL,
                    Permissions.EDUCATION_UPDATE, Permissions.EDUCATION_ADD,
                    Permissions.EDUCATION_USER_VIEW, Permissions.EDUCATION_DELETE,
                    Permissions.CHAPTER_LEAVE, Permissions.CHAPTER_JOIN,
                    Permissions.CHAPTER_VIEW, Permissions.CHAPTER_MEMBERS_VIEW,
                    Permissions.CHAPTER_VIEW_ALL,
                    Permissions.CAREER_UPDATE, Permissions.CAREER_ADD,
                    Permissions.CAREER_VIEW, Permissions.CAREER_DELETE,
//
                    Permissions.BIO_UPDATE, Permissions.BIO_ADD,
                    Permissions.BIO_VIEW, Permissions.BIO_DELETE,

                    Permissions.SKILLS_EDIT_SOFT,Permissions.SKILL_EDIT_TECHNICAL, Permissions.SKILL_LANGUAGE_EDIT,
                    Permissions.SKILL_SOFT_ADD,Permissions.SKILL_LANGUAGE_ADD,Permissions.SKILL_TECHNICAL_ADD,
                    Permissions.SKILLS_GET_LANGUAGE,Permissions.SKILLS_GET_SOFT, Permissions.SKILLS_GET_TECHNICAL,
                    Permissions.SKILL_DELETE

            )));
            roleRepository.save(alumniRole);
        }
    }
// hub admin permissions
@EventListener(ApplicationReadyEvent.class)
public void initializeHubAdminRole() {
    // Check if "HUB_ADMIN" role already exists
    Role hubAdminRole = roleRepository.findByRoleName("HUB_ADMIN");
    if (hubAdminRole == null) {
        hubAdminRole = new Role();
        hubAdminRole.setRoleName("HUB_ADMIN");
        hubAdminRole.setPermissions(new HashSet<>(Arrays.asList(
                Permissions.SOCIALS_UPDATE, Permissions.SOCIALS_ADD, Permissions.SOCIALS_VIEW,
                Permissions.PROFILE_UPDATE, Permissions.PROFILE_ADD, Permissions.PROFILE_VIEW,
                Permissions.PROFILE_VIEW_ALL, Permissions.PROFILE_DELETE,
                Permissions.FEEDS_UPDATE, Permissions.FEEDS_ADD, Permissions.FEEDS_VIEW,
                Permissions.FEEDS_USER_VIEW, Permissions.FEEDS_VIEW_ALL, Permissions.FEEDS_DELETE,
                Permissions.EVENTS_PARTICIPATE, Permissions.EVENTS_CANCEL_PARTICIPATION,
                Permissions.EVENTS_VIEW, Permissions.EVENTS_DATE_VIEW, Permissions.EVENTS_DATE_COUNT,
                Permissions.EVENTS_CHAPTER_VIEW, Permissions.EVENTS_CHAPTER_COUNT,
                Permissions.EVENTS_ACTIVE_VIEW, Permissions.EVENTS_ACTIVE_COUNT,
                Permissions.EVENTS_PAST_VIEW, Permissions.EVENTS_PAST_COUNT,
                Permissions.EVENTS_SCHEDULED_VIEW, Permissions.EVENTS_SCHEDULED_COUNT,
                Permissions.EVENTS_VIEW_ALL, Permissions.EVENTS_COUNT_ALL,
                Permissions.EDUCATION_UPDATE, Permissions.EDUCATION_ADD,
                Permissions.EDUCATION_USER_VIEW, Permissions.EDUCATION_DELETE,
                Permissions.CHAPTER_LEAVE, Permissions.CHAPTER_JOIN,
                Permissions.CHAPTER_VIEW, Permissions.CHAPTER_MEMBERS_VIEW,
                Permissions.CHAPTER_VIEW_ALL,
                Permissions.CAREER_UPDATE, Permissions.CAREER_ADD,
                Permissions.CAREER_VIEW, Permissions.CAREER_DELETE,
                Permissions.EVENTS_APPROVAL_VIEW,
                Permissions.HUB_APPROVAL,
                Permissions.APPROVE_HUB_EVENTS,
//
                Permissions.BIO_UPDATE, Permissions.BIO_ADD,
                Permissions.BIO_VIEW, Permissions.BIO_DELETE,

                Permissions.SKILLS_EDIT_SOFT,Permissions.SKILL_EDIT_TECHNICAL, Permissions.SKILL_LANGUAGE_EDIT,
                Permissions.SKILL_SOFT_ADD,Permissions.SKILL_LANGUAGE_ADD,Permissions.SKILL_TECHNICAL_ADD,
                Permissions.SKILLS_GET_LANGUAGE,Permissions.SKILLS_GET_SOFT, Permissions.SKILLS_GET_TECHNICAL,
                Permissions.SKILL_DELETE

        )));
        roleRepository.save(hubAdminRole);
    }
}
    @EventListener(ApplicationReadyEvent.class)
    public void initializeChapterLeaderRole() {
        // Check if "CHAPTER_LEADER" role already exists
        Role chapterLeaderRole = roleRepository.findByRoleName("CHAPTER_LEADER");
        if (chapterLeaderRole == null) {
            chapterLeaderRole = new Role();
            chapterLeaderRole.setRoleName("CHAPTER_LEADER");
            chapterLeaderRole.setPermissions(new HashSet<>(Arrays.asList(
                    Permissions.SOCIALS_UPDATE, Permissions.SOCIALS_ADD, Permissions.SOCIALS_VIEW,
                    Permissions.PROFILE_UPDATE, Permissions.PROFILE_ADD, Permissions.PROFILE_VIEW,
                    Permissions.PROFILE_VIEW_ALL, Permissions.PROFILE_DELETE,
                    Permissions.FEEDS_UPDATE, Permissions.FEEDS_ADD, Permissions.FEEDS_VIEW,
                    Permissions.FEEDS_USER_VIEW, Permissions.FEEDS_VIEW_ALL, Permissions.FEEDS_DELETE,
                    Permissions.EVENTS_UPDATE, Permissions.EVENTS_PARTICIPATE,
                    Permissions.EVENTS_CANCEL_PARTICIPATION, Permissions.EVENTS_CHAPTER_ADD,
                    Permissions.EVENTS_ADD, Permissions.EVENTS_VIEW, Permissions.EVENTS_DATE_VIEW,
                    Permissions.EVENTS_DATE_COUNT, Permissions.EVENTS_CHAPTER_VIEW,
                    Permissions.EVENTS_CHAPTER_COUNT, Permissions.EVENTS_ACTIVE_VIEW,
                    Permissions.EVENTS_ACTIVE_COUNT, Permissions.EVENTS_PAST_VIEW,
                    Permissions.EVENTS_PAST_COUNT, Permissions.EVENTS_SCHEDULED_VIEW,
                    Permissions.EVENTS_SCHEDULED_COUNT, Permissions.EVENTS_VIEW_ALL,
                    Permissions.EVENTS_COUNT_ALL, Permissions.EVENTS_DELETE,
                    Permissions.EDUCATION_UPDATE, Permissions.EDUCATION_ADD,
                    Permissions.EDUCATION_USER_VIEW, Permissions.EDUCATION_DELETE,
                    Permissions.CHAPTER_UPDATE, Permissions.CHAPTER_LEAVE,
                    Permissions.CHAPTER_JOIN, Permissions.CHAPTER_ADD,
                    Permissions.CHAPTER_VIEW, Permissions.CHAPTER_MEMBERS_VIEW,
                    Permissions.CHAPTER_VIEW_ALL, Permissions.CHAPTER_DELETE,
                    Permissions.CHAPTER_TYPE_UPDATE, Permissions.CHAPTER_TYPE_ADD,
                    Permissions.CHAPTER_TYPE_VIEW, Permissions.CHAPTER_TYPE_VIEW_ALL,
                    Permissions.CHAPTER_TYPE_DELETE, Permissions.CAREER_UPDATE,
                    Permissions.CAREER_ADD, Permissions.CAREER_VIEW, Permissions.CAREER_DELETE,
                    Permissions.APPROVE_CHAPTER_EVENTS, Permissions.EVENTS_APPROVAL_VIEW,


                    Permissions.BIO_UPDATE, Permissions.BIO_ADD, Permissions.BIO_VIEW,
                    Permissions.BIO_DELETE
            )));
            roleRepository.save(chapterLeaderRole);
        }
    }


    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public CustomResponse<List<RoleDto>> displayRoles() {
        CustomResponse<List<RoleDto>>response=new CustomResponse<>();
        try {
            List<Role> roleList = roleRepository.findAll();
            var result= roleList.stream().map(this::convertToDto).toList();
            if (result.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No roles found ");
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
    @Transactional
    public void updateRoleById(Long roleId, Role updatedRole) {
        Optional<Role> possibleExistingRole = roleRepository.findById(roleId);
        if (possibleExistingRole.isPresent()) {
            Role existingRole = possibleExistingRole.get();
            if (updatedRole.getRoleName() != null) {
                existingRole.setRoleName(updatedRole.getRoleName());
            }
            existingRole.setPermissions(updatedRole.getPermissions());
            roleRepository.save(existingRole);
        } else throw new NoResourceFoundException("The role to be update is not found");
    }


    @Override
    public CustomResponse<Role> displayRoleById(Long roleId) {
        CustomResponse<Role>response=new CustomResponse<>();
        try {
            Optional<Role> possibleRole = roleRepository.findById(roleId);
            if (possibleRole.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No roles found ");
                response.setPayload(null);}

            else {
                Role role=possibleRole.get();
                response.setPayload(role);
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
    public boolean deleteRole(Long roleId) throws NoResourceFoundException {
        Optional<Role> possibleRole = roleRepository.findById(roleId);
        if (possibleRole.isPresent()) {
            roleRepository.delete(possibleRole.get());
            return true;
        } else throw new NoResourceFoundException("role of id " + roleId + " does not exist");
    }

    private RoleDto convertToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }
}