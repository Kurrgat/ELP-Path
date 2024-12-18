package com.example.emtechelppathbackend.newsandupdates;

import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface NewsAndUpdatesService {
    CustomResponse<?> addNewsAndUpdates(NewsAndUpdatesDto newsAndUpdatesDto);

    CustomResponse<?> getAll();

    CustomResponse<?> updateNewsAndUpdates(Long id, NewsAndUpdatesDto newsAndUpdatesDto);

    CustomResponse<?> deleteNewsAndUpdatesById(Long id);

    CustomResponse<?> getById(Long id);


    CustomResponse<?> postViews(Long newsAndUpdateId, Long userId);
}
