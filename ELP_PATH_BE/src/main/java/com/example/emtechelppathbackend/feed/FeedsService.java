package com.example.emtechelppathbackend.feed;

import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.image.ImageRepository;
import com.example.emtechelppathbackend.utils.Base64Utils;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public interface FeedsService  {

    CustomResponse<List<FeedsDto>> getAllUsersFeeds();

    CustomResponse<List<FeedsDto>> getFeedById(Long Id);
    FeedsDto updateFeedsById(Long id, FeedsDto feedsDto);
    CustomResponse<List<FeedsDto>> getFeedByUserId(Long user_id);

    ResponseEntity<?> uploadFeeds(List<MultipartFile> files, Long userId, String title);


    static void uploadImages(Set<Image> images, MultipartFile file, ImageRepository imageRepository) throws IOException {
        String encodedData = Base64Utils.encode(file.getBytes());
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setData(encodedData);
        image.setType(file.getContentType());
        Image savedImage = imageRepository.save(image);
        images.add(savedImage);
    }

}