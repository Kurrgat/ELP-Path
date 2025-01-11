package com.example.emtechelppathbackend.auditing;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auditing")
@RequiredArgsConstructor
public class AuditingController {
    private final AuditingService auditingService;

    @GetMapping("/audit-logs")
    public ResponseEntity<?> getAuditInfo() {
      var response=auditingService.getAuditInfo();
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
