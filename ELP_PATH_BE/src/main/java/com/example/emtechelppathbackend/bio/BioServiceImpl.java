package com.example.emtechelppathbackend.bio;

import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BioServiceImpl implements BioService {
    private final BioRepository bioRepository;
    private final UsersRepository userRepository;

    public BioServiceImpl(BioRepository bioRepository, UsersRepository userRepository) {
        this.bioRepository = bioRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BioDto addBio(Long userId, BioDto bioDto) {
        Bio bio = mapToEntity(bioDto);
        Users user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));

        //if user already has a bio
        if (bioRepository.findBioByUserId(userId) != null){
            throw new UserDetailsNotFoundException("User already has a bio. You can edit your bio instead.");
        }

        //user doesn't have a bio
        bio.setUser(user);
        //bio.setId(bioDto.getId());
        //bio.setDescription(bio.getDescription());
        Bio newBio = bioRepository.save(bio);
        return mapToDto(newBio);
    }


    @Override
    public CustomResponse<BioDto> findBioByUserId(Long userId) {
        CustomResponse<BioDto> response= new CustomResponse<>();
        try {
            Bio bio = bioRepository.findBioByUserId(userId);

            if(bio != null) {
                var total= mapToDto(bio);
                response.setPayload(total);
                response.setMessage("Found");
                response.setStatusCode(HttpStatus.OK.value());
            } else {
                response.setSuccess(false);
                response.setMessage("Bio for user with id "+userId+" not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
            }

        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> updateBioByUserId(Long userId, Long bioId, BioDto bioDto) {
        CustomResponse<Bio> response = new CustomResponse<>();
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Bio bio = bioRepository.findById(bioId)
                    .orElseThrow(() -> new RuntimeException("Bio not found"));

            if (!Objects.equals(bio.getUser().getId(), user.getId())) {
                throw new RuntimeException("This bio does not belong to this user");
            }

            bio.setDescription(bioDto.getDescription());
            bio.setUser(user);
            bioRepository.save(bio);

            response.setMessage("Bio updated successfully");
            response.setSuccess(true);
            response.setStatusCode(HttpStatus.OK.value());
            response.setPayload(bio);
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setPayload(null);
        }

        return response;
    }

    @Override
    public CustomResponse<?> deleteBio(Long userId, Long bioId) {
        CustomResponse<Bio> response = new CustomResponse<>();
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Bio bio = bioRepository.findById(bioId)
                    .orElseThrow(() -> new RuntimeException("Bio not found"));
            if (!Objects.equals(bio.getUser().getId(), user.getId())) {
                throw new RuntimeException("This bio does not belong to this user");
            }
            bioRepository.delete(bio);
            response.setMessage("Bio deleted successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(null);
        } catch (Exception e) {
            response.setMessage("Error deleting bio: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    private BioDto  mapToDto(Bio bio){
        BioDto bioDto = new BioDto();
        bioDto.setId(bio.getId());
        bioDto.setDescription(bio.getDescription());
        return bioDto;
    }
    private Bio mapToEntity(BioDto bioDto){
        Bio bio = new Bio();
        bio.setId(bioDto.getId());
        bio.setDescription(bioDto.getDescription());
        return bio;
    }
}
