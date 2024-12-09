package com.example.emtechelppathbackend.feed.feedv2;

import com.example.emtechelppathbackend.chapter.*;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.HubsRepo;
import com.example.emtechelppathbackend.hubs.hubsv2.HubsRepov2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.profile.ProfileRepo;
import com.example.emtechelppathbackend.security.roles.Role;
import com.example.emtechelppathbackend.security.roles.RoleDto;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.emtechelppathbackend.security.user.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;


record Labels(Boolean toxic, Boolean severe_toxic,  Boolean obscene, Boolean threat, Boolean insult, Boolean identity_hate){}

@Service
@RequiredArgsConstructor
@Slf4j
class FeedsServiceImplv2 implements FeedsServicev2 {

    private final FeedsRepositoryv2 feedsRepository;
    private final UsersRepository usersRepository;
    private final ProfileRepo profileRepo;
    private final UsersRepository userRepo;
    private final JobOpportunityService jobOpportunityService;
    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    HostNameCapture hostNameCapture = new HostNameCapture();
    String imagesPath;
    @Autowired
    ServerProperties serverProperties;
    @Autowired
    ServerPortService serverPortService;







    @Override
    public CustomResponse<List<FeedsDTOv2>> getAllUsersFeeds(){
        CustomResponse<List<FeedsDTOv2>> response = new CustomResponse<>();

        try {
            List<FeedsV2> feeds = feedsRepository.findAll(Sort.by(Sort.Direction.DESC,"postDate"));
            if(feeds.isEmpty()) {
                response.setMessage("No feeds available");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {

                feeds.sort(Comparator.comparing(FeedsV2::getId).reversed());
                List<FeedsDTOv2> feedsList = feeds.stream().map(this::mapToDto).collect(Collectors.toList());



                response.setMessage(feedsList.size() == 1 ? "Found "+ feedsList.size()+" feed" : "Found "+feedsList.size()+" feeds.");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(feedsList);

            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                log.error(e.getMessage());
        }
        return response;
    }



    @Override
    public CustomResponse<?> uploadFeeds(Long userId, FeedsDTO feedsDTO) {
        CustomResponse<FeedsDTOv2> response = new CustomResponse<>();

        Set<String> images = new HashSet<>();
        FeedsV2 feed = new FeedsV2();

        try {
            Optional<Users> user = userRepo.findById(userId);

            if (user.isEmpty()) {
                response.setMessage("Not authorized to perform this operation");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                return response;
            }

            if (Objects.isNull(feedsDTO)) {
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                response.setMessage("At least a title and a description are required");
                return response;
            }

            if (Objects.isNull(feedsDTO.getDescription()) || Objects.isNull(feedsDTO.getTitle())) {
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setSuccess(false);
                response.setMessage("At least a title and a description are required");
                return response;
            }

            String content = feedsDTO.getTitle() + " " + feedsDTO.getDescription();

            Labels classification;
            try {
                String string = doClassification(content);
                if (string.equalsIgnoreCase("ERROR")) {
                    response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
                    response.setSuccess(false);
                    response.setMessage("Something went wrong!");
                    return response;
                }

                Gson gson = new Gson();
                classification = gson.fromJson(string, Labels.class);

            } catch (IOException | InterruptedException e) {
                response.setMessage(e.getMessage());
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return response;
            }

            feed.setToxic(classification.toxic());
            feed.setSevere_toxic(classification.severe_toxic());
            feed.setObscene(classification.obscene());
            feed.setThreat(classification.threat());
            feed.setInsult(classification.insult());
            feed.setIdentity_hate(classification.identity_hate());

            if (feedsDTO.getImages() != null && !feedsDTO.getImages().isEmpty()) {
                for (MultipartFile file : feedsDTO.getImages()) {
                    try {
                        String filename = file.getOriginalFilename();
                        if (Objects.requireNonNull(filename).endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".gif")) {
                            // Unique image filename
                            String extension = filename.substring(filename.lastIndexOf("."));
                            filename = filename.substring(0, filename.lastIndexOf("."));
                            String uniqueFileName = filename.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(Locale.ROOT) + extension;
                            log.info("feed images: " + uniqueFileName);
                            images.add(uniqueFileName);

                            // Save file
                            file.transferTo(uploadPath.resolve(uniqueFileName));
                        } else {
                            response.setMessage("Upload file with .jpeg, .png or gif or .jpg format");
                            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                            response.setSuccess(false);
                            return response;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        response.setMessage("Failed to upload feed.");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                        return response;
                    }
                }
            }


            feed.setDescription(feedsDTO.getDescription());
            feed.setImages(images);
            feed.setUser(user.get());

            feedsRepository.save(feed);

            FeedsDTOv2 feedsDTOv2 = mapToDto(feed);  // Convert FeedsV2 to FeedsDTOv2
            response.setMessage("Feed saved successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setSuccess(true);
            response.setPayload(feedsDTOv2);

        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
            response.setMessage("Failed to save feed: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }


    private static String doClassification(String desc) throws IOException, InterruptedException {
        String path = System.getProperty("user.dir");

        // Create a ProcessBuilder for the Python interpreter
        ProcessBuilder processBuilder = new ProcessBuilder("python",path+ "/ml/classifier.py", desc);
        Process process = processBuilder.start();

        // Read the output of the process
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }

            // Print error stream content
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    log.error(errorLine);
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "ERROR";
            }

            return stringBuilder.toString().trim();
        }
    }




    @Override
    public CustomResponse<FeedsDTOv2> getFeedById(Long id) {
        CustomResponse<FeedsDTOv2> response = new CustomResponse<>();


        try {
            Optional<FeedsV2> feeds = feedsRepository.findById(id);
            if(feeds.isEmpty()) {
                response.setMessage("feed with id "+id+" does not exist");
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            } else {
                Optional<FeedsDTOv2> feedDto = feeds.map(this::mapToDto);
                FeedsDTOv2 feed = feedDto.get();
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(feed);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            log.error(e.getMessage());
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateFeedsById(Long userId,Long id,  FeedsDTOv2 feedsDto) {
        CustomResponse<FeedsDTOv2> response = new CustomResponse<>();

        try {
            // Check if the user exists
            Optional<Users> userOptional = usersRepository.findById(userId);
            System.out.println(userOptional);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();

                // Check if the feed exists and belongs to the user
                Optional<FeedsV2> feedsV2Optional = feedsRepository.findByIdAndUserId(id, userId);
                System.out.println(feedsV2Optional);
                if (feedsV2Optional.isPresent()) {
                    FeedsV2 feed = feedsV2Optional.get();

                    // Update only if the corresponding field in the DTO is not null
                    if (feedsDto.getDescription() != null) {
                        feed.setDescription(feedsDto.getDescription());
                    }
                    if (feedsDto.getPostDate() != null) {
                        feed.setPostDate(LocalDateTime.parse(feedsDto.getPostDate()));
                    }

                    feedsRepository.save(feed);

                    FeedsDTOv2 feedsDtov2 = mapToDto(feed);

                    response.setMessage("Feed updated successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(feedsDtov2);
                } else {
                    response.setMessage("Feed not found for user with ID " + userId);
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                }
            } else {
                response.setMessage("User not found with ID " + userId);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setMessage("An error occurred while updating the feed");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }





    public CustomResponse<List<FeedsDTOv2>> getFeedByUserId(Long user_id) {
    CustomResponse<List<FeedsDTOv2>> response = new CustomResponse<>();

    try {
        Optional<Users> user = userRepo.findById(user_id);


        if (user.isEmpty()) {
            response.setMessage("User with id " + user_id + " not found");
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            return response;
        }

        List<FeedsV2> feedsList = feedsRepository.findByUser(user.get());

        List<FeedsDTOv2> feedsDTOList = feedsList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        response.setMessage("Feeds retrieved successfully");
        response.setStatusCode(HttpStatus.OK.value());
        response.setSuccess(true);
        response.setPayload(feedsDTOList);

    } catch (Exception e) {
        log.debug(e.getMessage());
        response.setMessage(e.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setSuccess(false);
        log.error(e.getMessage());
    }

    return response;
}



    @Override
    public CustomResponse<?> deleteFeed(Long id, Long userId) {
        CustomResponse<FeedsDTOv2> response = new CustomResponse<>();

        try {
            // Check if the user exists
            Optional<Users> userOptional = usersRepository.findById(userId);
            if (userOptional.isPresent()) {
                // Check if the feed exists and belongs to the user
                Optional<FeedsV2> feedOptional = feedsRepository.findByIdAndUserId(id, userId);
                if (feedOptional.isPresent()) {
                    feedsRepository.deleteById(id);

                    response.setMessage("Feed with id " + id + " deleted successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                } else {
                    response.setMessage("Feed with id " + id + " does not exist for user with id " + userId);
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                }
            } else {
                response.setMessage("User with id " + userId + " does not exist");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setMessage("An error occurred while deleting the feed");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }



    @Override
        public CustomResponse<?> getAllFeedsByHubId(Long hubId) {
            CustomResponse<List<FeedsDTOv2>> response = new CustomResponse<>();

            try {
                List<FeedsV2> feeds = feedsRepository.findByHubId(hubId, Sort.by(Sort.Direction.DESC, "postDate"));

                if (feeds.isEmpty()) {
                    response.setMessage("No feeds available for the given hub ID");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    List<FeedsDTOv2> feedsList = feeds.stream().map(this::mapToDto).collect(Collectors.toList());

                    response.setMessage(feedsList.size() == 1 ? "Found " + feedsList.size() + " feed" : "Found " + feedsList.size() + " feeds.");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setSuccess(true);
                    response.setPayload(feedsList);
                }
            } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                log.error(e.getMessage());
            }
            return response;
        }




    @Override
    public CustomResponse<?> getAllFeedsByChapterId(Long chapterId) {
        CustomResponse<List<FeedsDTOv2>> response = new CustomResponse<>();

        try {
            List<FeedsV2> feeds = feedsRepository.findByChapterId(chapterId, Sort.by(Sort.Direction.DESC, "postDate"));

            if (feeds.isEmpty()) {
                response.setMessage("No feeds available for the given chapter ID");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                List<FeedsDTOv2> feedsList = feeds.stream().map(this::mapToDto).collect(Collectors.toList());

                response.setMessage(feedsList.size() == 1 ? "Found " + feedsList.size() + " feed" : "Found " + feedsList.size() + " feeds.");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(feedsList);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error(e.getMessage());
        }
        return response;
    }


    private FeedsDTOv2 mapToDto(FeedsV2 feeds) {
        FeedsDTOv2 feedsDto = new FeedsDTOv2();
        feedsDto.setId(feeds.getId());

        feedsDto.setDescription(feeds.getDescription());
        feedsDto.setPostDate(feeds.getPostDate().toString());
        feedsDto.setToxic(feeds.getToxic());
        feedsDto.setSevereToxic(feeds.getSevere_toxic());
        feedsDto.setObscene(feeds.getObscene());
        feedsDto.setInsult(feeds.getInsult());
        feedsDto.setThreat(feeds.getThreat());
        feedsDto.setRegionOrInstitutionId(feeds.getRegionOrInstitutionId());




        feedsDto.setIdentityHate(feeds.getIdentity_hate());
        feedsDto.setHub(feeds.getHub());
        feedsDto.setChapter(feeds.getChapter());


        //convert Users entity to UsersDto
        UsersDto usersDto = getUsersDto(feeds);
        feedsDto.setUser(usersDto);

        feedsDto.setImages(feeds.getImages());
        return feedsDto;
    }

    private  UsersDto getUsersDto(FeedsV2 feeds) {
        Users users = feeds.getUser();
        UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());
        usersDto.setUserEmail(users.getUserEmail());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        Optional<String> option = profileRepo.findProfileImageByUserId(usersDto.getId());

        if (option.isPresent()) {
            String img = option.get();
            usersDto.setProfileImage(img);
        }
        //role entity to roleDTO
        Role role = users.getRole();
        RoleDto roleDto = new RoleDto();
        //set roleDTO fields
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getRoleName());
        usersDto.setRole(roleDto);
        return usersDto;
    }

    public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            try {
                int port = serverPortService.getPort();

                if(port > 1023) {
                    imagesPath = hostNameCapture.getHost()+":"+port+"/images/";
                } else {
                    log.debug("Port is reserved for system use");
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