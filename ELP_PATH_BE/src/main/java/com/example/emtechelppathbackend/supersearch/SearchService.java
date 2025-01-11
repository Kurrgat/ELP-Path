package com.example.emtechelppathbackend.supersearch;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SearchService {
   CustomResponse< List<SearchDto>> searchEntities(String keyword);
}
