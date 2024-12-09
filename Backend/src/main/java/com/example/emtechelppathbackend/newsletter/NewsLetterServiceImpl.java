package com.example.emtechelppathbackend.newsletter;

import com.example.emtechelppathbackend.newsandupdates.NewsAndUpdates;
import com.example.emtechelppathbackend.utils.CustomResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class NewsLetterServiceImpl implements NewsLetterService {
    @Autowired
    private final NewsLetterRepo newsLetterRepo;

    private final Path path = Paths.get(System.getProperty("user.dir")+"/images/");



    @Override
    public CustomResponse<?> addNewsLetter(NewsLetterDto newsLetterDto) {
        CustomResponse<NewsLetter>response=new CustomResponse<>();
        NewsLetter newsLetter=new NewsLetter();
        try {
            MultipartFile document= newsLetterDto.getDocument();
            if (document != null && !document.isEmpty()) {
                String originalFileName = document.getOriginalFilename();
                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : null;
                if (extension != null && (extension.equalsIgnoreCase(".pdf") )) {
                    // Generate a unique image name using the current timestamp
                    String uniqueName = System.currentTimeMillis() + extension;
                    newsLetter.setDocument(uniqueName);

                    // Transfer the  file to the destination path
                    document.transferTo(path.resolve(uniqueName));
                }else {
                    response.setMessage("Only pdf types are allowed.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }
            }
            newsLetter.setTitle(newsLetterDto.getTitle());
            newsLetter.setDescription(newsLetterDto.getDescription());

            newsLetterRepo.save(newsLetter);
            response.setMessage("Newsletter added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(newsLetter);

        }catch (Exception e){

            response.setMessage("failed to add Newsletter"+e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> deleteNewsLetterById(Long id) {
        CustomResponse<NewsLetter> response = new CustomResponse<>();
        try {
            Optional<NewsLetter> existingNewsLetter = newsLetterRepo.findById(id);

            if (existingNewsLetter.isPresent()) {
                // Newsletter exists, proceed with deletion
                newsLetterRepo.deleteById(id);

                response.setMessage("Newsletter has been deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            } else {
                // Newsletter does not exist, set appropriate response
                response.setMessage("Newsletter with id " + id + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {

            response.setMessage("Failed to delete Newsletter: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> findDetailsById(Long id) {
        CustomResponse<NewsLetterDetailsDto> response = new CustomResponse<>();

        try {
            Optional<NewsLetter> existingNewsLetter = newsLetterRepo.findById(id);

            if (existingNewsLetter.isPresent()) {
                NewsLetter newsLetter = existingNewsLetter.get();

                // Create a NewsLetterDetailsDTO object with selected fields
                NewsLetterDetailsDto detailsDTO = new NewsLetterDetailsDto();

                detailsDTO.setTitle(newsLetter.getTitle());
                detailsDTO.setDescription(newsLetter.getDescription());
                detailsDTO.setDate(newsLetter.getDate());


                response.setMessage("Newsletter found successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(detailsDTO);
            } else {
                response.setMessage("Newsletter with id " + id + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Failed to retrieve Newsletter details: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<?> updateNewsLetter(Long id, NewsLetterDto newsLetterDto) {
        CustomResponse<NewsLetter> response = new CustomResponse<>();
        try {
            Optional<NewsLetter> existingNewsLetter = newsLetterRepo.findById(id);
            if (existingNewsLetter.isPresent()){
                NewsLetter newsLetterUpdates = existingNewsLetter.get();
                newsLetterUpdates.setTitle(newsLetterDto.getTitle());
                newsLetterUpdates.setDescription(newsLetterDto.getDescription());
                MultipartFile documentFile = newsLetterDto.getDocument();
                if (documentFile != null && !documentFile.isEmpty()){
                    String originalFileName = documentFile.getOriginalFilename();
                    String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : null;
                    if (extension != null && (extension.equalsIgnoreCase(".pdf"))) {
                        // Generate a unique image name using the current timestamp
                        String uniqueName = System.currentTimeMillis() + extension;
                        newsLetterUpdates.setDocument(uniqueName);

                        // Transfer the image file to the destination path
                        documentFile.transferTo(path.resolve(uniqueName));


                    }else {
                        response.setMessage("Only pdf type is allowed.");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                        return response;
                    }
                }


                var result = newsLetterRepo.save(newsLetterUpdates);


                response.setMessage("NewsLetter edited successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(result);

            } else {
                response.setMessage("NewsLetter not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }


        }catch (Exception e){
            e.printStackTrace();
            response.setMessage("Failed to update Newsletter: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);

        }
        return response;
    }


}
