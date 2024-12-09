package com.example.emtechelppathbackend.newsandupdates;

import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsAndUpdatesServiceImpl implements NewsAndUpdatesService {
    @Autowired
    private final NewsAndUpdatesRepo newsAndUpdatesRepo;
    @Autowired
            private  final JobOpportunityService jobOpportunityService;

    HostNameCapture hostNameCapture=new HostNameCapture();
    private final Path path = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;
    @Autowired
    ServerPortService serverPortService;
    @Override
    public CustomResponse<?> addNewsAndUpdates(NewsAndUpdatesDto newsAndUpdatesDto) {
        CustomResponse<NewsAndUpdates>response=new CustomResponse<>();
        NewsAndUpdates newsAndUpdates =new NewsAndUpdates();
        try {
            MultipartFile imageFile = newsAndUpdatesDto.getImage();
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFileName = imageFile.getOriginalFilename();
                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : null;
                if (extension != null && (extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png"))) {
                    // Generate a unique image name using the current timestamp
                    String uniqueName = System.currentTimeMillis() + extension;
                    newsAndUpdates.setImage(uniqueName);

                    // Transfer the image file to the destination path
                    imageFile.transferTo(path.resolve(uniqueName));


                }else {
                    response.setMessage("Only JPEG, PNG, and JPG image types are allowed.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setSuccess(false);
                    return response;
                }
            }
            newsAndUpdates.setTitle(newsAndUpdatesDto.getTitle());
            newsAndUpdates.setMessage(newsAndUpdatesDto.getMessage());

            newsAndUpdatesRepo.save(newsAndUpdates);
            response.setMessage("Newsletter added successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(newsAndUpdates);

        }catch (Exception e){
            response.setMessage("failed to add News and updates"+e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getAll() {
        CustomResponse<List<NewsAndUpdates>> response = new CustomResponse<>();
        try {
            var result= newsAndUpdatesRepo.findAll();
            List<NewsAndUpdates> newsAndUpdatesListWithImages = result
                    .stream()
                    .filter(newsAndUpdates -> newsAndUpdates.getImage() != null && !newsAndUpdates.getImage().isEmpty())
                    .toList();
            if (newsAndUpdatesListWithImages.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("No newsUpdates with images found");
                response.setPayload(null);
                response.setSuccess(false);
            }else {
                for (NewsAndUpdates newsAndUpdates : newsAndUpdatesListWithImages) {
                    String imageUrl = getImagesPath() + newsAndUpdates.getImage();
                    newsAndUpdates.setImage(imageUrl);
                }
                response.setPayload(newsAndUpdatesListWithImages);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            }
        }catch (Exception e){
            response.setMessage("failed to get Newsletters"+e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    public CustomResponse<?> updateNewsAndUpdates(Long id, NewsAndUpdatesDto newsAndUpdatesDto) {
        CustomResponse<NewsAndUpdates> response = new CustomResponse<>();
        try {
            Optional<NewsAndUpdates> existingNewsLetter = newsAndUpdatesRepo.findById(id);

            if (existingNewsLetter.isPresent()) {
                NewsAndUpdates newsAndUpdates = existingNewsLetter.get();
                newsAndUpdates.setTitle(newsAndUpdatesDto.getTitle());
                newsAndUpdates.setMessage(newsAndUpdatesDto.getMessage());
                MultipartFile imageFile = newsAndUpdatesDto.getImage();
                if (imageFile != null && !imageFile.isEmpty()) {
                    String originalFileName = imageFile.getOriginalFilename();
                    String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.')) : null;
                    if (extension != null && (extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png"))) {
                        // Generate a unique image name using the current timestamp
                        String uniqueName = System.currentTimeMillis() + extension;
                        newsAndUpdates.setImage(uniqueName);

                        // Transfer the image file to the destination path
                        imageFile.transferTo(path.resolve(uniqueName));


                    }else {
                        response.setMessage("Only JPEG, PNG, and JPG image types are allowed.");
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setSuccess(false);
                        return response;
                    }
                }


                var result = newsAndUpdatesRepo.save(newsAndUpdates);


                response.setMessage("News and updates edited successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(result);

            } else {
                response.setMessage("News and updates not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {

            response.setMessage("Failed to update News and updates: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }
    @Override
    public CustomResponse<?> deleteNewsAndUpdatesById(Long id) {
        CustomResponse<NewsAndUpdates> response = new CustomResponse<>();
        try {
            Optional<NewsAndUpdates> existingNewsLetter = newsAndUpdatesRepo.findById(id);

            if (existingNewsLetter.isPresent()) {
                newsAndUpdatesRepo.deleteById(id);
                response.setMessage("News and updates has been deleted successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
            } else {
                // Newsletter does not exist, set appropriate response
                response.setMessage("News and updates with id " + id + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }
        } catch (Exception e) {

            response.setMessage("Failed to delete News and updates: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
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
}
