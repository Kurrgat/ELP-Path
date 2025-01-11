package com.example.emtechelppathbackend.scholarcv;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface ScholarCvService {

    CustomResponse<?> getCv(Long userId);
}
