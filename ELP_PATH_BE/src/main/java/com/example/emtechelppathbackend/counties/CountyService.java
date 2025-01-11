package com.example.emtechelppathbackend.counties;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountyService {
    private final CountyRepo countyRepo;

    public CustomResponse<?> fetchKenyanCounties(){
        CustomResponse<List<KenyanCounty>> response = new CustomResponse<>();

        try{
            List<KenyanCounty> counties = countyRepo.findAll();
            if (counties.isEmpty()){
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setMessage("No counties available");
            }else {
                response.setMessage("Found");
                response.setPayload(counties);
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }
}
