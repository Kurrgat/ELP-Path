package com.example.emtechelppathbackend.intern;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/interns")
public class InternController {
    @Autowired
    private final InternService internService;
    @PostMapping("/add-new-intern")
    public ResponseEntity<Intern> addNewIntern(@RequestBody Intern intern) {

        Intern internObj = internService.addNewIntern(intern);

        return new ResponseEntity<Intern>(internObj, HttpStatus.CREATED);
    }
}
