package com.example.emtechelppathbackend.chapter;

import com.example.emtechelppathbackend.chaptersmembers.ChapterMember;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMemberRepository;
import com.example.emtechelppathbackend.chaptersmembers.ChapterMemberService;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.image.ImageService;
import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarService;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChapterServiceImpl implements ChapterService {
    private final UsersRepository userRepository;
    private final ChapterRepo chapterRepo;
    private final ImageRepository imageRepository;
    private final ChapterMemberRepository chapterMemberRepository;
    private final ChapterMemberService chapterMemberService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;
    private final ChapterHandlerRepo chapterHandlerRepo;
    private final ChapterRepoV2 chapterRepoV2;
    private final ScholarService scholarService;


    @Override
    public void createChapter(Chapter chapter, MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()){
            Image image = imageService.handleImageUpload(file);
            chapter.setChapterImage(image);
        }
        chapterRepo.save(chapter);
    }

    @Override
    public CustomResponse<ChapterDto> getChapterById(Long chapterId, boolean includeCondition) {
        CustomResponse<ChapterDto> response = new CustomResponse<>();

        try {
            Optional<ChapterV2> optional = chapterRepoV2.getChapterById(chapterId, includeCondition);

            if(optional.isEmpty()) {
                response.setMessage("Not Found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(mapToDto(optional.get()));
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<List<ChapterDto>> getAllChapters(boolean includeCondition) {
        CustomResponse<List<ChapterDto>> response=new CustomResponse<>();
        try {
        var result = chapterRepoV2.getChapters(includeCondition);
        if(result.isEmpty()){
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No Chapter found");
            response.setPayload(null);
        }else {
            var chapters = result.stream().map(chapter -> mapToDto(chapter)).collect(Collectors.toList());
            response.setPayload(chapters);
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
    public CustomResponse<List<Scholar>> getChapterScholars(Long chapterId, boolean includeCondition) {
        CustomResponse<List<Scholar>> response=new CustomResponse<>();
        try {
        var result = chapterRepoV2.getChapterScholars(chapterId, includeCondition);
        if(result.isEmpty()){
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No Chapter found");
            response.setPayload(null);
        }else {
            // var chapters = result.stream().map(chapter -> mapToDto(chapter)).collect(Collectors.toList());
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
    public CustomResponse<List<ChapterDto>> getInstitutionChapterByCategory(String institutionCategory, boolean includeCondition){
        CustomResponse<List<ChapterDto>> response=new CustomResponse<>();
        try {
        var result = chapterRepoV2.getInstitutionChapterByCategory(institutionCategory, includeCondition);
        if(result.isEmpty()){
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No Chapter found");
            response.setPayload(null);
        }else {
            var chapters = result.stream().map(chapter -> mapToDto(chapter)).collect(Collectors.toList());
            response.setPayload(chapters);
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
    public CustomResponse<List<ChapterDto>> getInstitutionOrRegionalChapter(String chapterType, boolean includeCondition){
        CustomResponse<List<ChapterDto>> response=new CustomResponse<>();
        try {
        var result = chapterRepoV2.getInstitutionChapterByCategory(chapterType, includeCondition);
        if(result.isEmpty()){
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No Chapter found");
            response.setPayload(null);
        }else {
            var chapters = result.stream().map(chapter -> mapToDto(chapter)).collect(Collectors.toList());
            response.setPayload(chapters);
            response.setMessage("Found");
            response.setStatusCode(HttpStatus.OK.value());
        }
    }catch (Exception e){
        response.setMessage(e.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }
	 return response;
}


public CustomResponse<?> setChapterLeader(Long chapterId, Long scholarId, boolean includeCondition) {
    CustomResponse<String> response = new CustomResponse<>();

    try {
        Optional<ChapterV2> optional = chapterRepoV2.getChapterById(chapterId, includeCondition);
        if(optional.isEmpty()) {
            response.setMessage("Chapter not found");
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setSuccess(false);
        } else {
            ChapterV2 chapter = optional.get();
            Scholar scholar = scholarService.getScholarById(scholarId);

            if(scholar != null) {
                var chapterScholars = chapterRepoV2.getChapterScholars(chapterId, false);
                boolean scholarExists = chapterScholars.stream().anyMatch(element -> element.getId().equals(scholarId));

                if(!scholarExists) {
                    response.setMessage("Scholar does not beong to this chapter");
                    response.setStatusCode(HttpStatus.FORBIDDEN.value());
                    response.setSuccess(false);
                } else {
                    chapter.setChapterLeader(scholar);

                    chapterRepoV2.save(chapter);

                    response.setMessage("Successful");
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setPayload("User added as chapter admin");
                }
            } else {
                response.setMessage("Scholar with id "+scholarId+" not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
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





    //a user would like to join a hub
    @Override
    public Chapter joinChapter(Long userId, Long chapterId) {
        Users user = userRepository.findById(userId).orElseThrow(()->new NoResourceFoundException("User not found"));
        Chapter chapter =  chapterRepo.findById(chapterId).orElseThrow(()->new NoResourceFoundException("Chapter not found"));

        List<ChapterMember> existingMemberships = chapterMemberRepository.findByChapterAndMember(chapter, user);
        ChapterMember existingMembership = chapterMemberService.getActiveMembership(existingMemberships);

        if (existingMembership != null){
            throw new NoResourceFoundException("User is already a member of this chapter");
        }
        ChapterMember chapterMember = new ChapterMember();

        chapterMember.setChapter(chapter);
        chapterMember.setMember(user);
        chapterMember.setJoiningDate(LocalDateTime.now());
        chapterMember.setActiveMembership(true);

        chapterMemberRepository.save(chapterMember);

        return chapterRepo.save(chapter);
    }

    @Override
    public Chapter leaveChapter(Long userId, Long hubId) {
        Users users = userRepository.findById(userId).orElseThrow(()->new NoResourceFoundException("user not found"));
        Chapter chapter =  chapterRepo.findById(hubId).orElseThrow(()->new NoResourceFoundException("Chapter not found"));

        List<ChapterMember> chapterMembers = chapterMemberRepository.findByChapterAndMember(chapter, users);
        ChapterMember chapterMember = chapterMemberService.getActiveMembership(chapterMembers);

        if (chapterMember == null){
            throw new NoResourceFoundException("User is not a member of this chapter");
        }

        chapterMember.setLeavingDate(LocalDateTime.now());
        chapterMember.setActiveMembership(false);
        chapterMemberRepository.save(chapterMember);

        return chapterRepo.save(chapter);
    }
    @Override
    public Chapter updateChapterByChapterId(Long chapterId, ChapterDTOUpdate updatedChapterDto, MultipartFile chapterImage) throws NoResourceFoundException, IOException{
        Chapter existingChapter = chapterRepo.findById(chapterId)
                .orElseThrow(()->new UserDetailsNotFoundException("Chapter not found"));

        modelMapper.map(updatedChapterDto, existingChapter);

        if (chapterImage !=null && !chapterImage.isEmpty()){
            Image image = imageService.handleImageUpload(chapterImage);

            existingChapter.setChapterImage(image);
        }
        return chapterRepo.save(existingChapter);
    }


    @Override
    public void deleteChapter(Long chapterId){
        Chapter chapter = chapterRepo.findById(chapterId).orElseThrow(()->new RuntimeException("Chapter cannot be found"));
        chapterRepo.delete(chapter);
    }


    @Override
    public CustomResponse<Set<UsersDto>> getMembersByChapterId(Long chapterId) {
        CustomResponse<Set<UsersDto>>response=new CustomResponse<>();
        try {
            Set<ChapterMember> membersToDisplay = chapterMemberRepository.findByChapter_IdAndActiveMembershipIsTrue(chapterId);
            Set<Users> usersToDisplay = chapterMemberService.extractUsersFromChapterMembers(membersToDisplay);
          var result=  usersToDisplay.stream().map(this::convertUserToDto).collect(Collectors.toSet());
            if(result.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No members found");
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






    private ChapterDto mapToDto(ChapterV2 chapter){
        ChapterDto chapterDto = new ChapterDto();
        chapterDto.setId(chapter.getId());

        chapterDto.setChapterName(chapter.getNickName());
        // chapterDto.setImage(chapter.getImageUrl());
        chapterDto.setChapterType(chapter.getChapterType());
        Scholar scholar = chapter.getChapterLeader();
        if(scholar != null) {
            chapterDto.setLeaderId(scholar.getId());
            chapterDto.setLeaderName(scholar.getScholarFirstName()+" "+scholar.getScholarLastName());
        }
        return chapterDto;
    }

    // private Chapter matToEntity(ChapterDto chapterDto){
    //     Chapter chapter = new Chapter();
    //     chapter.setId(chapterDto.getId());

    //     chapter.setChapterName(chapterDto.getChapterName());
    //     chapter.setChapterType(chapterDto.getChapterType());
    //     chapter.setChapterDescription(chapterDto.getChapterDescription());
    //     chapter.setChapterImage(chapterDto.getChapterImage());

    //     return chapter;
    // }
    private UsersDto convertUserToDto(Users users){
        return modelMapper.map(users, UsersDto.class);
    }

    public CustomResponse<?> fetchChapterHandlers(){
        CustomResponse<List<ChapterHandlerInterface>> response = new CustomResponse<>();
        try{
            List<ChapterHandlerInterface> chapters = chapterHandlerRepo.fetchChapters();

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
        CustomResponse<ChapterHandlerInterface> response = new CustomResponse<>();
        try{
            Optional<ChapterHandlerInterface> chapters = chapterHandlerRepo.fetchChaptersThatScholarBelongsTo(scholarId);

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

    public CustomResponse<?> fetchAllChapterV2(){
        CustomResponse<List<ChapterV2>> response = new CustomResponse<>();

        try{
            List<ChapterV2> chapters = chapterRepoV2.findAll();

            if (chapters.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setPayload(List.of());
            }else {
                response.setPayload(chapters);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Found "+chapters.size()+" chapters");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }
}
