package com.example.emtechelppathbackend.hubs;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.hubmembers.HubMember;
import com.example.emtechelppathbackend.hubmembers.HubMemberRepo;
import com.example.emtechelppathbackend.hubmembers.HubMemberService;
import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.image.ImageService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HubServiceImpl implements HubService {
    private final UsersRepository userRepository;
    private final HubsRepo hubRepo;
    private final ImageRepository imageRepository;
    private final HubMemberRepo hubMemberRepository;
    private final HubMemberService hubMemberService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;
    HostNameCapture hostNameCapture = new HostNameCapture();
    String imagePath;


    @Override
    public void createHub(Hub hub, MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()){
            Image image = imageService.handleImageUpload(file);
            hub.setHubImage(image);
        }
        hubRepo.save(hub);
    }

    @Override
    public CustomResponse<HubDTO> getHubById(Long hubId) {
        CustomResponse<HubDTO>response=new CustomResponse<>();
        try {
            Hub hub = hubRepo.findById(hubId).orElseThrow(()->new UserDetailsNotFoundException("Hub not found"));
            var result= mapToDto(hub);
            if(result==null){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No activity found");
                response.setPayload(null);
            }else {

                response.setPayload(result);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return response;
    }

    @Override
    public CustomResponse<List<Hub>> getAllHubs() {
        CustomResponse<List<Hub>>response=new CustomResponse<>();
        try {
            var result= hubRepo.findAll();
            if(result.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No Hubs found");
                response.setPayload(null);
            }else {

                response.setPayload(result);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return response;
    }




    //a user would like to join a hub
    @Override
    public Hub joinHub(Long userId, Long hubId) {
        Users user = userRepository.findById(userId).orElseThrow(()->new NoResourceFoundException("User not found"));
        Hub hub =  hubRepo.findById(hubId).orElseThrow(()->new NoResourceFoundException("Hub not found"));

        List<HubMember> existingMemberships = hubMemberRepository.findByHubAndMember(hub, user);
        HubMember existingMembership = hubMemberService.getActiveMembership(existingMemberships);

        if (existingMembership != null){
            throw new NoResourceFoundException("User is already a member of this hub");
        }

        HubMember hubMember = new HubMember();

        hubMember.setHub(hub);
        hubMember.setMember(user);
        hubMember.setJoiningDate(LocalDateTime.now());
        hubMember.setActiveMembership(true);

        hubMemberRepository.save(hubMember);

        return hubRepo.save(hub);
    }

    @Override
    public Hub leaveHub(Long userId, Long hubId) {
        Users users = userRepository.findById(userId).orElseThrow(()->new NoResourceFoundException("user not found"));
        Hub hub =  hubRepo.findById(hubId).orElseThrow(()->new NoResourceFoundException("Hub not found"));

        List<HubMember> chapterMembers = hubMemberRepository.findByHubAndMember(hub, users);
        HubMember hubMember = hubMemberService.getActiveMembership(chapterMembers);

        if (hubMember == null){
            throw new NoResourceFoundException("User is not a member of this hub");
        }

        hubMember.setLeavingDate(LocalDateTime.now());
        hubMember.setActiveMembership(false);
        hubMemberRepository.save(hubMember);

        return hubRepo.save(hub);
    }
    @Override
    public Hub updateHubById(Long hubId, HubDTOUpdate updatedHubDto, MultipartFile hubImage) throws NoResourceFoundException, IOException{
        Hub existingHub = hubRepo.findById(hubId)
                .orElseThrow(()->new UserDetailsNotFoundException("Hub not found"));

        modelMapper.map(updatedHubDto, existingHub);

        if (hubImage !=null && !hubImage.isEmpty()){
            Image image = imageService.handleImageUpload(hubImage);

            existingHub.setHubImage(image);
        }
        return hubRepo.save(existingHub);
    }


    @Override
    public void deleteHub(Long hubId){
        Hub hub = hubRepo.findById(hubId).orElseThrow(()->new RuntimeException("Hub cannot be found"));
        hubRepo.delete(hub);
    }

    @Override
    public CustomResponse<Set<UsersDto>> getMembersByHubId(Long hubId) {
        CustomResponse<Set<UsersDto>>response=new CustomResponse<>();
        try {
            Set<HubMember> membersToDisplay = hubMemberRepository.findByHub_IdAndActiveMembershipIsTrue(hubId);
            Set<Users> usersToDisplay = hubMemberService.extractUsersFromHubMembers(membersToDisplay);
            var result= usersToDisplay.stream().map(this::convertUserToDto).collect(Collectors.toSet());
            if(result.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No member found");
                response.setPayload(null);
            }else {

                response.setPayload(result);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return response;
    }




    private HubDTO mapToDto(Hub hub){
        HubDTO hubDto = new HubDTO();
        hubDto.setId(hub.getId());
        hubDto.setHubDescription(hub.getHubDescription());
        hubDto.setHubName(hub.getHubName());
        hubDto.setHubImage(hub.getHubImage());
        return hubDto;
    }

    private Hub matToEntity(HubDTO hubDto){
        Hub hub = new Hub();
        hub.setId(hubDto.getId());

        hub.setHubName(hubDto.getHubName());
        hub.setHubDescription(hubDto.getHubDescription());
        hub.setHubImage(hubDto.getHubImage());

        return hub;
    }
    private UsersDto convertUserToDto(Users users){
        return modelMapper.map(users, UsersDto.class);
    }
}
