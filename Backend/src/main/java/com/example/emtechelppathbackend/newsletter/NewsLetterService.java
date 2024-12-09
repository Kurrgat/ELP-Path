package com.example.emtechelppathbackend.newsletter;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface NewsLetterService {
    CustomResponse<?> addNewsLetter(NewsLetterDto newsLetterDto);

    CustomResponse<?> deleteNewsLetterById(Long id);

    CustomResponse<?> findDetailsById(Long id);

    CustomResponse<?>updateNewsLetter(Long id, NewsLetterDto newsLetterDto);


    ;
}
