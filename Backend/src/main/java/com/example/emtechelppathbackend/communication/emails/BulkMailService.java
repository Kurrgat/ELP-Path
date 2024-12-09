package com.example.emtechelppathbackend.communication.emails;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface BulkMailService {
    CustomResponse<?> sendToAll(String subject, String body);
    CustomResponse<?> sendToChapterMembers(Long chapterId, String message, String body);
}
