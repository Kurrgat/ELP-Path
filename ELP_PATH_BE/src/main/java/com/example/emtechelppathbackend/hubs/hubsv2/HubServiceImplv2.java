package com.example.emtechelppathbackend.hubs.hubsv2;

import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberServicev2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.notification.Notification;
import com.example.emtechelppathbackend.notification.NotificationRepository;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.security.roles.RoleRepository;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.MembershipStatus;
import com.example.emtechelppathbackend.utils.ServerPortService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
@Slf4j
public class HubServiceImplv2 implements HubServicev2 {
    private final UsersRepository userRepository;
    private final HubsRepov2 hubRepo;
    private final HubMemberRepov2 hubMemberRepository;
    private final HubMemberServicev2 hubMemberService;
    private final ModelMapper modelMapper;
    private final JobOpportunityService jobOpportunityService;
    private final NotificationRepository notificationRepository;
    private final RoleRepository roleRepository;

    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    HostNameCapture hostNameCapture = new HostNameCapture();
    String imagesPath;

    @Autowired
    ServerPortService serverPortService;


    @Override
    public CustomResponse<?> createHub(Hubv2 hub, MultipartFile file) throws IOException {
        CustomResponse<Hubv2> response = new CustomResponse<>();

        try {

            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String extension = StringUtils.getFilenameExtension(originalFileName); // Using Spring's utility to get file extension

                // Check if the file extension is valid
                if (isValidImageExtension(extension)) { // Assuming this method exists and is accessible here
                    // Generate a unique file name using the current timestamp
                    String uniqueName = System.currentTimeMillis() + "." + extension;
                    hub.setHubImage(uniqueName);

                    // Transfer the file to the specified upload path
                    file.transferTo(uploadPath.resolve(uniqueName));

                    hubRepo.save(hub);

                    response.setMessage("Hub added successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(hub);
                } else {
                    response.setMessage("Only JPEG, PNG, JPG, image types are allowed.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                }
            }

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    private boolean isValidImageExtension(String extension) {
        return extension != null && (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png"));
    }


    @Override
    public CustomResponse<?> getHubByUserId(Long userId) {
        CustomResponse<List<HubDTOv2>> response = new CustomResponse<>();

        try {
            // Attempt to find membership records for the user across hubs
            List<HubMemberv2> memberships = hubMemberRepository.findMembershipByUserId(userId);

            if (memberships.isEmpty()) {
                response.setMessage("User with id " + userId + " is not a member of any hub");
                response.setStatusCode(HttpStatus.NOT_FOUND.value()); // More appropriate for no memberships found
                response.setSuccess(false);
                return response;
            }

            // Extract hubs from the membership records
            List<Hubv2> hubs = memberships.stream()
                    .map(HubMemberv2::getHub)
                    .collect(Collectors.toList());

            // Optionally, filter out inactive memberships or duplicate hubs if necessary

            // Enhance hub details if needed, like setting the image URL
            hubs.forEach(hub -> {
                if (hub.getHubImage() != null && !hub.getHubImage().isEmpty()) {
                    String imageUrl = jobOpportunityService.getImagesPath() + hub.getHubImage();
                    hub.setHubImage(imageUrl);
                }
            });

            response.setMessage("Hubs found");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(mapToDtoList(hubs)); // Ensure you have a method to map a list of hubs to their DTOs
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    private List<HubDTOv2> mapToDtoList(List<Hubv2> hubs) {
        if (hubs == null || hubs.isEmpty()) {
            return new ArrayList<>();
        }

        // Use Java Stream API to map each Hubv2 entity to a HubDTOv2 object
        return hubs.stream().map(hub -> {
            HubDTOv2 dto = new HubDTOv2();
            dto.setId(hub.getId());
            dto.setHubName(hub.getHubName());
            dto.setHubDescription(hub.getHubDescription());
            dto.setHubImage(hub.getHubImage());
            return dto;
        }).collect(Collectors.toList());
    }



    @Override
    public CustomResponse<List<Hubv2>> getAllHubs() {
        CustomResponse<List<Hubv2>> response = new CustomResponse<>();

        try {
            List<Hubv2> hubs = hubRepo.findAll();

            for(Hubv2 hub: hubs) {
                if(hub.getHubImage() != null && !hub.getHubImage().isEmpty()) {
                    String hubImage = jobOpportunityService.getImagesPath()+hub.getHubImage();
                    hub.setHubImage(hubImage);
                }
            }

            response.setMessage("Found "+hubs.size()+" hubs");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(hubs);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> joinHub(Long userId, Long hubId) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUser = userRepository.findById(userId);
            Optional<Hubv2> optionalHub = hubRepo.findById(hubId);

            if (optionalUser.isEmpty()) {
                response.setMessage("User with ID " + userId + " does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            if (optionalHub.isEmpty()) {
                response.setMessage("Hub not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            Users user = optionalUser.get();
            Hubv2 hub = optionalHub.get();

            // Check if the user is already a member of this hub
            List<HubMemberv2> existingMemberships = hubMemberRepository.findByHubAndMember(hub, user);
            HubMemberv2 existingMembership = hubMemberService.getActiveMembership(existingMemberships);

            if (existingMembership != null) {
                response.setMessage("User is already a member of this hub");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Check if there is a pending membership request for the user in this hub
            List<HubMemberv2> pendingMemberships = hubMemberRepository.findByHubAndMemberAndStatus(hub, user, MembershipStatus.PENDING);
            if (!pendingMemberships.isEmpty()) {
                response.setMessage("Membership request already exists for this user in this hub");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            // Create a pending membership for the user
            HubMemberv2 hubMemberv2 = new HubMemberv2();
            hubMemberv2.setHub(hub);
            hubMemberv2.setMember(user);
            hubMemberv2.setJoiningDate(LocalDateTime.now());
            hubMemberv2.setActiveMembership(false);
            hubMemberv2.setStatus(MembershipStatus.PENDING);

            hubMemberRepository.save(hubMemberv2);

            response.setMessage("Membership request sent for approval");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload("User's membership request for the hub has been sent for approval");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }




    @Override
    public CustomResponse<String> leaveHub(Long userId, Long hubId) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUser = userRepository.findById(userId);
            Optional<Hubv2> optionalHub = hubRepo.findById(hubId);

            if (optionalUser.isEmpty()) {
                response.setMessage("User does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else if (optionalHub.isEmpty()) {
                response.setMessage("Hub not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                List<HubMemberv2> hubMembers = hubMemberRepository.findByHubAndMember(optionalHub.get(), optionalUser.get());
                HubMemberv2 hubMember = hubMemberService.getActiveMembership(hubMembers);

                if (hubMember == null) {
                    response.setMessage("User is not a member of this hub");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    // Set the leaving date to now
                    hubMember.setLeavingDate(LocalDateTime.now());
                    hubMember.setActiveMembership(false);
                    hubMember.setStatus(MembershipStatus.INACTIVE); // This line assumes you have a status field to update

                    hubMemberRepository.save(hubMember);

                    response.setMessage("Member has successfully left the hub");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload("Member has successfully left the hub");
                }
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<Hubv2> updateHubById(Long hubId, HubDTOUpdatev2 updatedHubDto, MultipartFile hubImage) throws NoResourceFoundException, IOException{
        CustomResponse<Hubv2> response = new CustomResponse<>();

        try {
            Optional<Hubv2> optionalHub = hubRepo.findById(hubId);

            if(optionalHub.isEmpty()) {
                response.setMessage("Hub with id "+hubId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Hubv2 existingHub = optionalHub.get();
                modelMapper.map(updatedHubDto, existingHub);

                if (hubImage !=null && !hubImage.isEmpty()){
                    String imageName = hubImage.getOriginalFilename();
                    String uniqueName = imageName+ updatedHubDto.getHubName();

                    hubImage.transferTo(uploadPath.resolve(uniqueName));

                    existingHub.setHubImage(uniqueName);

                    hubRepo.save(existingHub);

                    response.setMessage("Hub updated successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(existingHub);
                }
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        
        return response;
    }


    @Override
    public CustomResponse<?> deleteHub(Long hubId) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Hubv2> hubOptional = hubRepo.findById(hubId);
            if (hubOptional.isEmpty()) {
                response.setMessage("Hub cannot be found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            // Retrieve hub members associated with the hub
            List<HubMemberv2> hubMembers = hubMemberRepository.findByHubId(hubId);

            // Delete hub members
            hubMemberRepository.deleteAll(hubMembers);

            // Delete the hub
            hubRepo.deleteById(hubId);

            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload("Hub with id " + hubId + " deleted successfully");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getHubMembersByHubId(Long hubId) {
        CustomResponse<List<HubMemberRepov2.HubMemberInterface1>> response = new CustomResponse<>();
        try {
            // Check if the hub exists
            Optional<Hubv2> optionalHub = hubRepo.findById(hubId);
            if (optionalHub.isEmpty()) {
                response.setMessage("Hub does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
                return response;
            }

            // Fetch active members for the hub
            List<HubMemberRepov2.HubMemberInterface1> membersToDisplay = hubMemberRepository.findByHubIdAndActiveMembershipIsTrue1(hubId);

            if (membersToDisplay.isEmpty()) {
                response.setMessage("No users for this hub");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                response.setMessage("Found " + membersToDisplay.size() + " hub members");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(membersToDisplay);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception stack trace
            response.setMessage("An error occurred while fetching hub members: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }


    @Override
    public CustomResponse<?> setHubAdmin(Long hubId, Long userId, boolean includeCondition) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Hubv2> optionalHub = hubRepo.getHubById(hubId, includeCondition);

            if (optionalHub.isEmpty()) {
                response.setMessage("Hub not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            Hubv2 hub = optionalHub.get();

            // Fetch the user by ID
            Optional<Users> optionalUser = userRepository.getUserById(userId);

            if (optionalUser.isEmpty()) {
                response.setMessage("User not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            Users user = optionalUser.get();

            // Fetch active hub members considering the includeCondition if applicable
            List<HubMemberRepov2.HubMemberInterface> hubMembers = hubMemberRepository.getHubMembersByHubId(hubId);
            boolean userIsAnActiveMember = hubMembers.stream()
                    .anyMatch(member -> userId.equals(member.getUserId()));


            if (!userIsAnActiveMember) {
                response.setMessage("User is not a member of this hub or their membership is not active");
                response.setStatusCode(HttpStatus.FORBIDDEN.value());
                response.setSuccess(false);
                return response;
            }

            hub.setHubAdmin(user);
            Role hubAdminRole = roleRepository.findByRoleName("HUB_ADMIN");
            user.setRole(hubAdminRole);
            hubRepo.save(hub);

            response.setMessage("User set as hub admin successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload("User set as hub admin");
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }



    @Override
    public CustomResponse<?> approveJoiningHub(Long membershipId, boolean approve) {
        CustomResponse<String> response = new CustomResponse<>();
        try {
            Optional<HubMemberv2> optionalHubMember = hubMemberRepository.findById(membershipId);
            if (optionalHubMember.isEmpty()) {
                response.setMessage("Membership request not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            HubMemberv2 hubMember = optionalHubMember.get();
            if ((approve && hubMember.getStatus() == MembershipStatus.APPROVED) ||
                    (!approve && hubMember.getStatus() == MembershipStatus.DECLINED)) {
                response.setMessage("Membership is already in the desired state");
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                return response;
            }

            hubMember.setStatus(approve ? MembershipStatus.APPROVED : MembershipStatus.DECLINED);
            hubMember.setActiveMembership(approve);
            if (approve) {
                hubMember.setJoiningDate(LocalDateTime.now());
            }
            hubMemberRepository.saveAndFlush(hubMember);

            Hubv2 hub = hubMember.getHub(); // Fetch the hub associated with the member
            Users user = hubMember.getMember(); // Fetch the member to avoid LazyInitializationException

            CompletableFuture.runAsync(() -> {
                if (user != null && approve) { // Check added to handle approval inside async call
                    saveNotificationForUser(hub, user, approve);
                }
            }).exceptionally(ex -> {
                System.err.println("Error in sending notifications: " + ex.getMessage());
                return null; // Handling exception inside CompletableFuture
            });

            response.setMessage(approve ? "Membership approved successfully" : "Membership declined successfully");
            response.setPayload(approve ? "Approved" : "Declined");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("Error processing request: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }




    private void saveNotificationForUser(Hubv2 hub, Users user, boolean approved) {
        try {
            String message = approved ? "Membership approved for Hub: " + hub.getHubName() : "Membership declined for Hub: " + hub.getHubName();
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUsers(user);
            notification.setHubs(hub);
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to save notification: " + e.getMessage());
        }
    }



    @Override
    public CustomResponse<?> getMembershipForApproval() {
        CustomResponse<List<HubMemberRepov2.HubMemberInterface1>> response = new CustomResponse<>();

        try {
            // Fetch only memberships that are pending approval
            List<HubMemberRepov2.HubMemberInterface1> hubMembers = hubMemberRepository.findByStatus(MembershipStatus.PENDING);

            if (hubMembers.isEmpty()) {
                response.setMessage("No memberships pending approval");
                response.setStatusCode(HttpStatus.OK.value()); // Or consider HttpStatus.NO_CONTENT if you prefer
                response.setSuccess(false);
                response.setPayload(Collections.emptyList()); // Consider returning an empty list to indicate no results explicitly
            } else {
                response.setMessage("Memberships pending approval found");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(hubMembers);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<Hubv2> getHub(Long hubId) {
        CustomResponse<Hubv2> response = new CustomResponse<>();

        try {
            // Fetch the hub by ID
            Optional<Hubv2> optional = hubRepo.findById(hubId);

            if (optional.isEmpty()) {
                response.setMessage("Hub not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value()); // Returning HttpStatus.NOT_FOUND for clarity
                response.setSuccess(false);
            } else {
                response.setMessage("Hub found");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(optional.get()); // Ensure that you are setting the actual hub, not the optional
            }
        } catch (Exception e) {
            response.setMessage("An error occurred: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }



    private HubDTOv2 mapToDto(Hubv2 hub){
        HubDTOv2 hubDto = new HubDTOv2();
        hubDto.setId(hub.getId());
        hubDto.setHubDescription(hub.getHubDescription());
        hubDto.setHubName(hub.getHubName());
        hubDto.setHubImage(hub.getHubImage());
        return hubDto;
    }

    private UsersDto convertUserToDto(Users users){
        return modelMapper.map(users, UsersDto.class);
    }

    

    public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            try {
                int port = serverPortService.getPort();

                if(port > 1023) {
                    imagesPath = hostNameCapture.getHost()+":"+port+"/images/";
                } else {
                    log.debug("Port is reservered for system use");
                }
                // imagesPath = hostNameCapture.getHost()+":5555/images/";
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagesPath;
    }
}
