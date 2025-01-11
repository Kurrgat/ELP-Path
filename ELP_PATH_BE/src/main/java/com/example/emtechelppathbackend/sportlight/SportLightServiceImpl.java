package com.example.emtechelppathbackend.sportlight;

import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdates;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.ext.awt.image.SpotLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SportLightServiceImpl implements SportLightService {
    private final SportLightRepository sportLightRepository;
    HostNameCapture hostNameCapture=new HostNameCapture();
    private final Path path = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;
    @Autowired
    ServerPortService serverPortService;

    @Override
    public CustomResponse<SportLight> addSportLight(SportLightDto sportLightDto) {
        CustomResponse<SportLight> response = new CustomResponse<>();
        SportLight sportLight = new SportLight();
        try {
            MultipartFile file = sportLightDto.getImage();
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String extension = StringUtils.getFilenameExtension(originalFileName); // Using Spring's utility to get file extension

                // Check if the file extension is valid
                if (isValidImageExtension(extension)) {
                    // Generate a unique file name using the current timestamp
                    String uniqueName = System.currentTimeMillis() + "." + extension;
                    sportLight.setImage(uniqueName);

                    // Transfer the file to the destination path
                    file.transferTo(path.resolve(uniqueName));
                } else {
                    response.setMessage("Only JPEG, PNG, JPG, image types are allowed.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }
            }

            sportLight.setTitle(sportLightDto.getTitle());
            sportLight.setContent(sportLightDto.getContent());
            sportLight.setVideoUrl(sportLightDto.getVideoUrl());

            sportLightRepository.save(sportLight);

            response.setMessage("Sport light successfully onboarded");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(sportLight);
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
    public CustomResponse<List<SportLight>> getAll() {
        CustomResponse<List<SportLight>> response = new CustomResponse<>();
        try {
            // Fetch all SportLight entities sorted by createdAt in descending order
            List<SportLight> sportLights = sportLightRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            if (sportLights.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No spotlight with images found");
                response.setPayload(null);
                response.setSuccess(false);
            } else {
                // Modify the file URLs
                for (SportLight sportLight : sportLights) {
                    String imageUrl = getImagesPath() + sportLight.getImage();
                    sportLight.setImage(imageUrl);
                }
                response.setPayload(sportLights);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }



    @Override
    public CustomResponse<SportLight> getById(Long id) {
        CustomResponse<SportLight> response = new CustomResponse<>();
        try {
            var result = sportLightRepository.findById(id);

            if (result.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No spotlight found");
                response.setPayload(null);
            } else {
                SportLight sportLight = result.get();

                if (sportLight.getImage() != null && !sportLight.getImage().isEmpty()) {
                    // Image exists
                    String imageUrl = getImagesPath() + sportLight.getImage();
                    sportLight.setImage(imageUrl);
                }
                    // Image doesn't exist
                    response.setPayload(sportLight);
                    response.setMessage("Successful");
                    response.setSuccess(true);
                    response.setStatusCode(HttpStatus.OK.value());

            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public CustomResponse<SportLight> updateSportLight(Long id, SportLightDto sportLightDto) {
        CustomResponse<SportLight> response = new CustomResponse<>();

        try {
            Optional<SportLight> existingSportLightOptional = sportLightRepository.findById(id);

            if (existingSportLightOptional.isPresent()) {
                SportLight sportLight = existingSportLightOptional.get();

                sportLight.setTitle(sportLightDto.getTitle());
                sportLight.setContent(sportLightDto.getContent());
                sportLight.setVideoUrl(sportLightDto.getVideoUrl());

                MultipartFile imageFile = sportLightDto.getImage();

                if (imageFile != null && !imageFile.isEmpty()) {
                    String originalFileName = imageFile.getOriginalFilename();
                    String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : null;

                    if (extension != null && (extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png"))) {
                        // Generate a unique image name using the current timestamp
                        String uniqueName = System.currentTimeMillis() + extension;
                        sportLight.setImage(uniqueName);

                        // Transfer the image file to the destination path
                        imageFile.transferTo(path.resolve(uniqueName));
                    } else {
                        response.setMessage("Only JPEG, PNG, and JPG image types are allowed.");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                        return response;
                    }
                }

                // Save the updated sportLight
                SportLight updatedSportLight = sportLightRepository.save(sportLight);

                response.setMessage("SportLight updated successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(updatedSportLight);
            } else {
                response.setMessage("SportLight with id " + id + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
        }

        return response;
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
    public CustomResponse<SportLight> deleteSportLightById(Long id) {
        CustomResponse<SportLight> response = new CustomResponse<>();
        try {
            if (sportLightRepository.existsById(id)) {
                sportLightRepository.deleteById(id);

                response.setMessage("Spotlight with ID " + id + " has been deleted");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            } else {
                response.setMessage("Spotlight not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


}



