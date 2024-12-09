package com.example.emtechelppathbackend.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    public Image handleImageUpload(MultipartFile imageFile) throws IOException{
        Image image = new Image();

        try {
            image.setName(imageFile.getOriginalFilename());
             byte[] imageBytes = imageFile.getBytes();
            String base64ImageString = Base64.getEncoder().encodeToString(imageBytes);

            image.setData(base64ImageString);
            image.setType(imageFile.getContentType());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

       
        return imageRepository.save(image);
    }
}
