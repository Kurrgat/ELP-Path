package com.example.emtechelppathbackend.intern;

import com.example.emtechelppathbackend.scholars.Scholar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternService {
    private final InternRepo internRepo;
    public Intern addNewIntern(Intern intern) {
        return internRepo.save(intern);
    }
}
