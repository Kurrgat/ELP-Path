package com.example.emtechelppathbackend.comment;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
   CustomResponse<?>addComment(Long userId,Long feedId, CommentDto commentDto);

    CustomResponse<?> getTotalComments( Long feedId);

    CustomResponse<?> getAllComments(Long feedId);
}
