package com.example.emtechelppathbackend.auditing;

import com.example.emtechelppathbackend.bio.Bio;
import com.example.emtechelppathbackend.bio.BioDto;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditingServiceImpl implements AuditingService{
    private final AuditingRepo auditingRepo;
    private final UsersRepository usersRepository;

    @Override
    public CustomResponse<?> getAuditInfo() {
        CustomResponse<List<Auditing>> response= new CustomResponse<>();
        try {
            List<Auditing> auditingList=auditingRepo.findAll();

            if (auditingList.isEmpty()){
                response.setMessage("no auditing information");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            }else {
                response.setMessage("auditing information fetched successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(auditingList);
            }
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    public void logLogin(HttpServletRequest request, String userEmail) {
        Users user = usersRepository.findUsersByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auditing audit = new Auditing();
        audit.setUserEmail(userEmail);
        audit.setLoginAttempt(LocalDateTime.now());
//        audit.setLastActive(user.getLastModified());
        audit.setIpAddress(request.getRemoteAddr());
        audit.setOperatingSystem(request.getHeader("User-Agent"));
        audit.setActive(true);

    auditingRepo.save(audit);
    }
}
