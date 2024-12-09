package com.example.emtechelppathbackend.chapter.chapterv2;

import com.example.emtechelppathbackend.chapter.ChapterRepoV2;
import com.example.emtechelppathbackend.chapter.ChapterV2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberRepositoryv2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberServicev2;
import com.example.emtechelppathbackend.chaptersmembers.chaptermembersv2.ChapterMemberV2;
import com.example.emtechelppathbackend.counties.CountyRepo;
import com.example.emtechelppathbackend.counties.KenyanCounty;
import com.example.emtechelppathbackend.education.Institution;
import com.example.emtechelppathbackend.education.InstitutionRepo;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChapterServiceImplv2 implements ChapterServicev2 {
    private final UsersRepository userRepository;
    private final ChapterRepoV2 chapterRepo;
    private final ChapterMemberRepositoryv2 chapterMemberRepository;
    private final ChapterMemberServicev2 chapterMemberService;
    private final ModelMapper modelMapper;
    private final ChapterHandlerRepov2 chapterHandlerRepo;
    private final JobOpportunityService jobOpportunityService;
    private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;

    @Autowired
    ServerProperties serverProperties;
    @Autowired
    CountyRepo countyRepo;
    @Autowired
    InstitutionRepo institutionRepo;


    @Override
    public CustomResponse<?> createChapter(ChapterDtov2 chapterDtov2) {
        CustomResponse<ChapterV2> response = new CustomResponse<>();
        ChapterV2 chapterV2 = new ChapterV2();

        try {
            MultipartFile imageFile = chapterDtov2.getChapterImage();

            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFileName = imageFile.getOriginalFilename();
                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : null;

                if (extension != null && (extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png"))) {
                    // Generate a unique image name using the current timestamp
                    String uniqueName = System.currentTimeMillis() + extension;
                    chapterV2.setChapterImage(uniqueName);

                    // Transfer the image file to the destination path
                    imageFile.transferTo(uploadPath.resolve(uniqueName));
                } else {
                    response.setMessage("Only JPEG, PNG, and JPG image types are allowed.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }
            }

            // Set other properties
            chapterV2.setChapterName(chapterDtov2.getChapterName());
            chapterV2.setChapterDescription(chapterDtov2.getChapterDescription());
            chapterV2.setNickName(chapterDtov2.getNickName());
            chapterV2.setRegionOrInstitutionId(chapterDtov2.getRegionOrInstitutionId());
            chapterV2.setChapterType(chapterDtov2.getChapterType());
            chapterV2.setChapterLeader(chapterDtov2.getChapterLeader());

            // Save the chapter to the repository
            chapterRepo.save(chapterV2);

            response.setMessage("New chapter created");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(chapterV2);
        } catch (IOException e) {
            response.setMessage("Failed to process the file. Please try again.");
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            log.error("IOException during file processing: " + e.getMessage(), e);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public CustomResponse<ChapterDtov2> getChapterById(Long chapterId) {
        CustomResponse<ChapterDtov2> response = new CustomResponse<>();

        try {
            Optional<ChapterV2> optionalChapter = chapterRepo.findById(chapterId);
            
            if(optionalChapter.isEmpty()) {
                response.setMessage("Chapter not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                ChapterV2 chapter = optionalChapter.get();

                if(chapter.getChapterImage() != null && !chapter.getChapterImage().isEmpty()) {
                    String imageUrl = jobOpportunityService.getImagesPath()+chapter.getChapterImage();
                    chapter.setChapterImage(imageUrl);
                }

                response.setMessage("Chapter found");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(mapToDto(chapter));
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getAllChapters() {
        CustomResponse<List<ChapterV2>> response = new CustomResponse<>();

        try {
            List<ChapterV2> chapters = chapterRepo.findAll();

            for(ChapterV2 chapter: chapters) {
                if(chapter.getChapterImage() != null && !chapter.getChapterImage().isEmpty()) {
                    String chapterImage = jobOpportunityService.getImagesPath()+chapter.getChapterImage();
                    chapter.setChapterImage(chapterImage);
                }
            }

            response.setMessage("Found "+chapters.size()+" chapters");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(chapters);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    //a user would like to join a hub
    @Override
    public CustomResponse<?> joinChapter(Long userId, Long chapterId) {
        CustomResponse<String> response = new CustomResponse<>();

        try {
            Optional<Users> optionalUsers = userRepository.findById(userId);
            Optional<ChapterV2> optionalChapter = chapterRepo.findById(chapterId);

            if(optionalUsers.isEmpty()) {
                response.setMessage("User not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                if(optionalChapter.isEmpty()) {
                    response.setMessage("Chapter not found");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    List<ChapterMemberV2> exisitingMemberships = chapterMemberRepository.findByChapterAndMember(optionalChapter.get(), optionalUsers.get());
                    ChapterMemberV2 existingMembership = chapterMemberService.getActiveMembership(exisitingMemberships);

                    if (existingMembership != null){
                        response.setMessage("User is already a member of this chapter");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                    } else {              
                        ChapterMemberV2 chapterMember = new ChapterMemberV2();

                        chapterMember.setChapter(optionalChapter.get());
                        chapterMember.setMember(optionalUsers.get());
                        chapterMember.setJoiningDate(LocalDateTime.now());
                        chapterMember.setActiveMembership(true);

                        chapterMemberRepository.save(chapterMember);

                        response.setMessage(HttpStatus.OK.getReasonPhrase());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setSuccess(true);
                        response.setPayload("User added to chapter");
                    }
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
    public CustomResponse<?> leaveChapter(Long userId, Long hubId) {
        CustomResponse<String> response = new CustomResponse<>(); 

        try {
            Optional<Users> optionalUser = userRepository.findById(userId);
            Optional<ChapterV2> optionalChapter = chapterRepo.findById(hubId);

            if(optionalUser.isEmpty()) {
                response.setMessage("User not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                if(optionalChapter.isEmpty()) {
                    response.setMessage("Chapter not found");
                    response.setStatusCode(HttpStatus.NOT_FOUND.value());
                    response.setSuccess(false);
                } else {
                    ChapterV2 chapter = optionalChapter.get();
                    List<ChapterMemberV2> chapterMembers = chapterMemberRepository.findByChapterAndMember(optionalChapter.get(), optionalUser.get());
                    ChapterMemberV2 chapterMember = chapterMemberService.getActiveMembership(chapterMembers);

                    if (chapterMember == null){
                        response.setMessage("User is not a member of this chapter");
                        response.setStatusCode(HttpStatus.NOT_FOUND.value());
                        response.setSuccess(false);
                    } else {
                        chapterMember.setLeavingDate(LocalDateTime.now());
                        chapterMember.setActiveMembership(false);

                        chapterMemberRepository.save(chapterMember);
                        chapterRepo.save(chapter);

                        response.setMessage(HttpStatus.OK.getReasonPhrase());
                        response.setStatusCode(HttpStatus.OK.value());
                        response.setSuccess(true);
                        response.setPayload("User left chapter on "+chapterMember.getLeavingDate());
                    }
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
    public CustomResponse<ChapterDtov2> updateChapterByChapterId(Long chapterId, ChapterDTOUpdatev2 updatedChapterDto, MultipartFile chapterImage) throws NoResourceFoundException, IOException{
        CustomResponse<ChapterDtov2> response = new CustomResponse<>();

        try {
            Optional<ChapterV2> optionalChapter = chapterRepo.findById(chapterId);

            if(optionalChapter.isEmpty()) {
                response.setMessage("Chapter not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                ChapterV2 chapter = optionalChapter.get();
                modelMapper.map(updatedChapterDto, chapter);

                

                if (chapterImage !=null && !chapterImage.isEmpty()){
                    String fileName = chapterImage.getOriginalFilename();
                    String uniqueName = fileName + chapter.getChapterName()+chapter.getId();

                    chapterImage.transferTo(uploadPath.resolve(uniqueName));
                    chapter.setChapterImage(uniqueName);
                }
                chapterRepo.save(chapter);
                ChapterDtov2 body = modelMapper.map(chapter, ChapterDtov2.class);

                response.setMessage("Chapter updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(body);
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }

        return response;
    }


    @Override
    public CustomResponse<?> deleteChapter(Long chapterId){
        CustomResponse<String> response = new CustomResponse<>();

        try {
            if(!chapterRepo.existsById(chapterId)) {
                response.setMessage("Chapter not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                chapterRepo.deleteById(chapterId);

                response.setMessage("Deleted");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload("Chapter with id "+chapterId+" deleted successfully");
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<Set<UsersDto>> getMembersByChapterId(Long chapterId) {
        CustomResponse<Set<UsersDto>> response = new CustomResponse<>();

        try {
            Set<ChapterMemberV2> membersToDisplay = chapterMemberRepository.findByChapter_IdAndActiveMembershipIsTrue(chapterId);
            Set<Users> usersToDisplay = chapterMemberService.extractUsersFromChapterMembers(membersToDisplay);

            if (membersToDisplay.isEmpty()) {
                response.setMessage("Users are yet to join this Chapter");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                response.setMessage("Chapter has "+membersToDisplay.size()+" members");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(usersToDisplay.stream().map(this::convertUserToDto).collect(Collectors.toSet()));
            }
        } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setSuccess(false);
        }
        return response;

    }

    private ChapterDtov2 mapToDto(ChapterV2 chapter ){
        ChapterDtov2 chapterDto = new ChapterDtov2();

        chapterDto.setChapterDescription(chapter.getChapterDescription());
        chapterDto.setChapterName(chapter.getChapterName());
       chapterDto.setNickName(chapter.getNickName());



        return chapterDto;
    }

    private UsersDto convertUserToDto(Users users){
        return modelMapper.map(users, UsersDto.class);
    }

    public CustomResponse<?> fetchChapterHandlers(){
        CustomResponse<List<ChapterHandlerInterfacev2>> response = new CustomResponse<>();
        try{
            List<ChapterHandlerInterfacev2> chapters = chapterHandlerRepo.fetchChapters();

            if (chapters.isEmpty()){
                response.setSuccess(false);
                response.setMessage("No chapter members in place");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }else {
                response.setMessage("Found");
                response.setPayload(chapters);
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public CustomResponse<?> fetchChaptersThatScholarBelongsTo(Long scholarId){
        CustomResponse<ChapterHandlerInterfacev2> response = new CustomResponse<>();
        try{
            Optional<ChapterHandlerInterfacev2> chapters = chapterHandlerRepo.fetchChaptersThatScholarBelongsTo(scholarId);

            if (chapters.isEmpty()){
                response.setSuccess(false);
                response.setMessage("The scholar does not belong to any chapter");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }else {
                response.setMessage("Found");
                response.setPayload(chapters.get());
                response.setStatusCode(HttpStatus.OK.value());
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            if(serverProperties != null) {
                imagesPath = hostNameCapture.getHost()+":"+serverProperties.getPort()+"/images/";
                System.out.println(imagesPath);
            } else {
                // imagesPath = hostNameCapture.getHost()+":"+hostNameCapture.getPort()+"/images/";
                imagesPath = hostNameCapture.getHost()+":5555/images/";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagesPath;
    }
}
