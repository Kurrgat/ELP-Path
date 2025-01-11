package com.example.emtechelppathbackend.sportlight;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SportLightService {


   CustomResponse<List<SportLight>>  getAll();

    CustomResponse<SportLight> getById(Long id);

   CustomResponse<SportLight> updateSportLight(Long id, SportLightDto sportLightDto);

    CustomResponse<SportLight> addSportLight(SportLightDto sportLightDto);

    CustomResponse<?> deleteSportLightById(Long id);
}
