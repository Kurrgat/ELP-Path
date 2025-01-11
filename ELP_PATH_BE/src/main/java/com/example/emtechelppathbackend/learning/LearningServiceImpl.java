package com.example.emtechelppathbackend.learning;

import com.example.emtechelppathbackend.learning.enrol.Enrol;
import com.example.emtechelppathbackend.learning.enrol.EnrolDto;
import com.example.emtechelppathbackend.learning.enrol.EnrolRepository;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class LearningServiceImpl implements LearningService{
    private final LearningRepository learningRepo;
    private final UsersRepository userRepository;
    private  final EnrolRepository enrolRepository;
    private final Path path = Paths.get(System.getProperty("user.dir")+"/images/");

    String imagesPath;
    @Autowired
    ServerPortService serverPortService;

    @Override
    public CustomResponse<?> addCourse(LearningDto learningDto) {
        CustomResponse<LearningEntity> response = new CustomResponse<>();
        LearningEntity learningEntity = new LearningEntity();
        try {
            MultipartFile imageFile = learningDto.getImage();
            MultipartFile documentFile = learningDto.getDocument();

            // Handle image file
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalImageFileName = imageFile.getOriginalFilename();
                String imageExtension = StringUtils.getFilenameExtension(originalImageFileName);

                // Check if the image file extension is valid
                if (!isValidImageExtension(imageExtension)) {
                    response.setMessage("Only JPEG, PNG, JPG image types are allowed for images.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }

                // Generate a unique image file name using the current timestamp
                String uniqueImageName = System.currentTimeMillis() + "." + imageExtension;
                learningEntity.setImage(uniqueImageName);

                // Transfer the image file to the destination path
                imageFile.transferTo(path.resolve(uniqueImageName));
            }

            // Handle document file
            if (documentFile != null && !documentFile.isEmpty()) {
                String originalDocumentFileName = documentFile.getOriginalFilename();
                String documentExtension = StringUtils.getFilenameExtension(originalDocumentFileName);


                // Generate a unique document file name using the current timestamp
                String uniqueDocumentName = System.currentTimeMillis() + "." + documentExtension;
                learningEntity.setDocument(uniqueDocumentName);

                // Transfer the document file to the destination path
                documentFile.transferTo(path.resolve(uniqueDocumentName));
            }

            // Set other attributes of learningEntity
            learningEntity.setTitle(learningDto.getTitle());
            learningEntity.setDescription(learningDto.getDescription());
            learningEntity.setLink(learningDto.getLink());
            learningEntity.setCourseName(learningDto.getCourseName());
            learningEntity.setObjectives(learningDto.getObjectives());
            learningEntity.setCategory(learningDto.getCategory());

            // Save learningEntity to the repository
            learningRepo.save(learningEntity);

            response.setMessage("Learning course added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(learningEntity);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }




    @Override
    public CustomResponse<?> getLearningCourse() {
        CustomResponse<List<LearningEntity>> response = new CustomResponse<>();
        try {
            // Fetch all SportLight entities sorted by createdAt in descending order
            List<LearningEntity> learningEntities = learningRepo.findAll();

            if (learningEntities.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No learning course found");
                response.setPayload(null);
                response.setSuccess(false);
            } else {
                // Modify the file URLs
                for (LearningEntity learningEntity : learningEntities) {
                    String imageUrl = getImagesPath() + learningEntity.getImage();
                    String documentUrl=getImagesPath() +learningEntity.getDocument();
                    learningEntity.setImage(imageUrl);
                    learningEntity.setDocument(documentUrl);
                }
                response.setPayload(learningEntities);
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
    public CustomResponse<?> editLearningCourse(LearningDto learningDto, Long learningId) {
        CustomResponse<LearningEntity> response = new CustomResponse<>();
        try {
            // Fetch the existing LearningEntity from the repository
            Optional<LearningEntity> optionalLearningEntity = learningRepo.findById(learningId);
            if (optionalLearningEntity.isPresent()) {
                LearningEntity existingLearningEntity = optionalLearningEntity.get();

                MultipartFile file = learningDto.getImage();
                if (file != null && !file.isEmpty()) {
                    String originalFileName = file.getOriginalFilename();
                    String extension = StringUtils.getFilenameExtension(originalFileName);

                    // Check if the file extension is valid
                    if (isValidImageExtension(extension)) {
                        // Generate a unique file name using the current timestamp
                        String uniqueName = System.currentTimeMillis() + "." + extension;
                        existingLearningEntity.setImage(uniqueName);

                        // Transfer the file to the destination path
                        file.transferTo(path.resolve(uniqueName));
                    } else {
                        response.setMessage("Only JPEG, PNG, JPG image types are allowed.");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                        return response;
                    }
                }
                MultipartFile documentFile = learningDto.getDocument();

                // Handle document file
                if (documentFile != null && !documentFile.isEmpty()) {
                    String originalDocumentFileName = documentFile.getOriginalFilename();
                    String documentExtension = StringUtils.getFilenameExtension(originalDocumentFileName);


                    // Generate a unique document file name using the current timestamp
                    String uniqueDocumentName = System.currentTimeMillis() + "." + documentExtension;
                    existingLearningEntity.setDocument(uniqueDocumentName);

                    // Transfer the document file to the destination path
                    documentFile.transferTo(path.resolve(uniqueDocumentName));
                }

                existingLearningEntity.setTitle(learningDto.getTitle());
                existingLearningEntity.setDescription(learningDto.getDescription());
                existingLearningEntity.setLink(learningDto.getLink());
                existingLearningEntity.setCourseName(learningDto.getCourseName());
                existingLearningEntity.setObjectives(learningDto.getObjectives());
                existingLearningEntity.setCategory(learningDto.getCategory());

                // Save the updated LearningEntity
                learningRepo.save(existingLearningEntity);

                response.setMessage("Learning course edited successfully.");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(existingLearningEntity);
            } else {
                response.setMessage("Learning course not found.");
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

    @Override
    public CustomResponse<?> deleteLearningCourse(Long learningId) {
        CustomResponse<LearningEntity> response = new CustomResponse<>();
        try {
            // Check if the learning entity exists
            Optional<LearningEntity> optionalLearningEntity = learningRepo.findById(learningId);
            if (optionalLearningEntity.isPresent()) {
                // If it exists, delete it
                LearningEntity existingLearningEntity = optionalLearningEntity.get();
                learningRepo.deleteById(learningId);

                response.setMessage("Learning course deleted successfully.");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(null);
            } else {
                // If it doesn't exist, return a not found response
                response.setMessage("Learning course not found.");
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

    @Override
    public CustomResponse<?> enrolCourse(EnrolDto enrolDto, Long learningId, Long userId) {
        CustomResponse<Enrol> response = new CustomResponse<>();
        try {
            // Create an instance of Enrol entity
            Enrol enrol = new Enrol();

            // Fetch the User and LearningEntity based on their IDs
            Optional<Users> optionalUser = userRepository.findById(userId);
            Optional<LearningEntity> optionalLearningEntity = learningRepo.findById(learningId);

            if (optionalUser.isPresent() && optionalLearningEntity.isPresent()) {
                Users user = optionalUser.get();
                LearningEntity learningEntity = optionalLearningEntity.get();

                // Set the user and learningEntity in the Enrol entity
                enrol.setUsers(user);
                enrol.setLearning(learningEntity);
                enrol.setEnrol(true);

                // Save the Enrol entity
                enrolRepository.save(enrol);

                response.setMessage("Course enrolled successfully.");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            } else {
                response.setMessage("User or learning course not found.");
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


    private boolean isValidImageExtension(String extension) {
        return extension != null && (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png"));
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
