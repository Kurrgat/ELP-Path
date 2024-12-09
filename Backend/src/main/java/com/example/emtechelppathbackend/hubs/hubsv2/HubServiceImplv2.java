package com.example.emtechelppathbackend.hubs.hubsv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;

import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberRepov2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberServicev2;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    HostNameCapture hostNameCapture = new HostNameCapture();
    String imagesPath;

    @Autowired
    ServerPortService serverPortService;


    @Override
    public CustomResponse<?> createHub(Hubv2 hub, MultipartFile file) throws IOException {
        CustomResponse<Hubv2> response = new CustomResponse<>();


        try {
            if (file != null && !file.isEmpty()){
                String fileName = file.getOriginalFilename();

                if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                    String uniqueName = fileName+hub.getHubName();

                    file.transferTo(uploadPath.resolve(uniqueName));
                    hub.setHubImage(uniqueName);

                    hubRepo.save(hub);

                    response.setMessage("Hub added successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(hub);
                } else {
                    response.setMessage("upload files of type .jpeg, .jpg or .png");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                }
        } else {
                    hubRepo.save(hub);

                    response.setMessage("Hub added successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(hub);
        }

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
        
    }

    @Override
    public CustomResponse<HubDTOv2> getHubById(Long hubId) {
        CustomResponse<HubDTOv2> response = new CustomResponse<>();

        try {
            Optional<Hubv2> hubOptional = hubRepo.findById(hubId);
            if(hubOptional.isEmpty()) {
                response.setMessage("Hub with id "+hubId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Hubv2 hub = hubOptional.get();

                if(hubOptional.get().getHubImage() != null && !hubOptional.get().getHubImage().isEmpty()) {
                    String imageUrl = jobOpportunityService.getImagesPath()+hubOptional.get().getHubImage();
                    hub.setHubImage(imageUrl);
                }
                response.setMessage("Hub found");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(mapToDto(hub));
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
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

    //a user would like to join a hub
    @Override
    public CustomResponse<?> joinHub(Long userId, Long hubId) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUser = userRepository.findById(userId);
            Optional<Hubv2> optionalHub =  hubRepo.findById(hubId);
            if(optionalUser.isEmpty()) {
                response.setMessage("User with id "+userId+" does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                if(optionalHub.isEmpty()) {
                    response.setMessage("Hub not found");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    List<HubMemberv2> existingMemberships = hubMemberRepository.findByHubAndMember(optionalHub.get(), optionalUser.get()); 
                    HubMemberv2 existingMembership = hubMemberService.getActiveMembership(existingMemberships);

                    if(existingMembership != null) {
                        response.setMessage("User is already a member of this hub");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    }
                    HubMemberv2 hubMemberv2 = new HubMemberv2();

                    hubMemberv2.setHub(optionalHub.get());
                    hubMemberv2.setMember(optionalUser.get());
                    hubMemberv2.setJoiningDate(LocalDateTime.now());
                    hubMemberv2.setActiveMembership(true);

                    hubMemberRepository.save(hubMemberv2);

                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload("User added to hub successfully");
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
    public CustomResponse<?> leaveHub(Long userId, Long hubId) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUser = userRepository.findById(userId);
            Optional<Hubv2> optionalHub =  hubRepo.findById(hubId);

            if(optionalUser.isEmpty()) {
                response.setMessage("User does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                if(optionalHub.isEmpty()) {
                    response.setMessage("Hub not found");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    Hubv2 hub = optionalHub.get();
                    List<HubMemberv2> hubMembers = hubMemberRepository.findByHubAndMember(optionalHub.get(), optionalUser.get());
                    HubMemberv2 hubMemberv2 = hubMemberService.getActiveMembership(hubMembers);

                    if(hubMemberv2 == null) {
                        response.setMessage("User is not a member of this hub");
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setSuccess(false);
                    } else {
                        hubMemberv2.setLeavingDate(LocalDateTime.now());
                        hubMemberv2.setActiveMembership(false);

                        hubMemberRepository.save(hubMemberv2);
                        hubRepo.save(hub);
                    }

                    response.setMessage(HttpStatus.OK.getReasonPhrase());
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload("Member has left hub on "+LocalDateTime.now().toLocalDate());
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
    public CustomResponse<?> deleteHub(Long hubId){
        CustomResponse<String> response = new CustomResponse<>();

        try {
            if(!hubRepo.existsById(hubId)) {
                response.setMessage("Hub cannot be found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                hubRepo.deleteById(hubId);

                response.setMessage(HttpStatus.OK.getReasonPhrase());
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload("Hub with id "+hubId+" deleted successfully");
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<Set<UsersDto>> getMembersByHubId(Long hubId) {
        CustomResponse<Set<UsersDto>> response = new CustomResponse<>();

        try {
            if(!hubRepo.existsById(hubId)) {
                response.setMessage("Hub does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                Set<HubMemberv2> membersToDisplay = hubMemberRepository.findByHub_IdAndActiveMembershipIsTrue(hubId);
                Set<Users> hubMembers = hubMemberService.extractUsersFromHubMembers(membersToDisplay);

                if (membersToDisplay.isEmpty()) {
                    response.setMessage("No users for this hub");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                    response.setPayload(hubMembers.stream().map(this::convertUserToDto).collect(Collectors.toSet()));
                } else {
                    response.setMessage("Found "+hubMembers.size()+" hub members");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(hubMembers.stream().map(this::convertUserToDto).collect(Collectors.toSet()));
                }
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
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
