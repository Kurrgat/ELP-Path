package com.example.emtechelppathbackend.feed;

import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;

import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.security.roles.RoleDto;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;

import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;


@Service
class FeedsServiceImpl implements FeedsService {

    private final FeedsRepository feedsRepository;
    private final UsersRepository userRepository;
    private final ImageRepository imageRepository;


    public FeedsServiceImpl(FeedsRepository feedsRepository, UsersRepository userRepository, ImageRepository imageRepository) {
        this.feedsRepository = feedsRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }



    @Override
    public CustomResponse<List<FeedsDto>> getAllUsersFeeds(){
        CustomResponse <List<FeedsDto>> response = new CustomResponse<>();
        try {
            var total= feedsRepository.findAll()
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            if(total==null){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No event found");
                response.setPayload(null);
            }else {
                response.setPayload(total);
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
    public ResponseEntity<?> uploadFeeds(List<MultipartFile> files, Long userId,String description) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new UserDetailsNotFoundException("user with that id not found"));
        Set<Image> images = new HashSet<>();
        Feeds feeds = new Feeds();

        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    FeedsService.uploadImages(images, file, imageRepository);
                } catch (IOException e) {
                    return new ResponseEntity<>("Failed to upload feed.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        feeds.setUser(users);
        feeds.setDescription(description);
        feeds.setImages(images);
        Feeds savedFeeds = feedsRepository.save(feeds);
        return new ResponseEntity<>(savedFeeds, HttpStatus.CREATED);
    }
    @Override
    public CustomResponse<List<FeedsDto>> getFeedById(Long id) {
        CustomResponse<List<FeedsDto>> response=new CustomResponse<>();
        try{
            var total= feedsRepository.findById(id)
                    .stream()
                    .map(this::mapToDto).collect(Collectors.toList());
            if(total.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No event found");
                response.setPayload(null);
            }else {
                response.setPayload(total);
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
    public FeedsDto updateFeedsById(Long id, FeedsDto feedsDto) {
        Feeds feed = feedsRepository.findById(id).orElseThrow();
        feed.setDescription(feedsDto.getDescription());
        feed.setPostDate(feedsDto.getPostDate());
        return mapToDto(feed);
    }


    @Override
    public CustomResponse<List<FeedsDto>> getFeedByUserId(Long user_id) {
        CustomResponse<List<FeedsDto>> response = new CustomResponse<>();
        try {
            var total= feedsRepository.findAllByUserId(user_id)
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors
                            .toList());
            if(total==null){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No feeds found");
                response.setPayload(null);
            }else {
                response.setPayload(total);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return response;
    }



    private FeedsDto mapToDto(Feeds feeds) {
        FeedsDto feedsDto = new FeedsDto();
        feedsDto.setId(feeds.getId());
        feedsDto.setDescription(feeds.getDescription());
        feedsDto.setPostDate(feeds.getPostDate());
        //convert Users entity to UsersDto
        Users users = feeds.getUser();
        UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());
        usersDto.setUserEmail(users.getUserEmail());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());

        //role entity to roleDTO
        Role role = users.getRole();
        RoleDto roleDto = new RoleDto();
        //set roleDTO fields
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getRoleName());
        usersDto.setRole(roleDto);
        feedsDto.setUser(usersDto);

        feedsDto.setImage(feeds.getImages());
        return feedsDto;
    }

}