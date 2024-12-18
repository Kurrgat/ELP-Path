package com.example.emtechelppathbackend.auditing;

import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuditingService {
    CustomResponse<?> getAuditInfo();
}
