package com.example.emtechelppathbackend.like;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {
    CustomResponse<?>saveLike(Long userId,Long feedId, LikeEntity like);

    CustomResponse<?> deleteLike(Long id);

    CustomResponse<?>getTotalLikes(Long feedId);

    CustomResponse<?> getAllLikes(Long feedId);
}
