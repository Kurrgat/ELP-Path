package com.example.emtechelppathbackend.supersearch;

import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchRepository customRepository;
    public CustomResponse<List<SearchDto>> searchEntities(String keyword) {
        CustomResponse<List<SearchDto>>response=new CustomResponse<>();
        try {
            var result= customRepository.searchEntities(keyword);
            if (result.isEmpty()) {
                response.setMessage("Search not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {

                response.setMessage("search retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(result);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }

        return response;
    }
}



