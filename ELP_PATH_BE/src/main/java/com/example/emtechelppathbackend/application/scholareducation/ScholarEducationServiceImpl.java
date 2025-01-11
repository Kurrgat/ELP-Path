package com.example.emtechelppathbackend.application.scholareducation;

import com.example.emtechelppathbackend.application.wtf_application.Application;
/*import com.example.emtechelppathbackend.entities.Scholar;*/
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.UserDetailsNotFoundException;
import com.example.emtechelppathbackend.application.wtf_application.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScholarEducationServiceImpl implements ScholarEducationService {
    private final ApplicationRepository applicationRepository;
    private final ScholarEducationRepo scholarEducationRepo;

    public ScholarEducationServiceImpl(ApplicationRepository applicationRepository, ScholarEducationRepo scholarEducationRepo) {
        this.applicationRepository = applicationRepository;
        this.scholarEducationRepo = scholarEducationRepo;
    }

    @Override
    public ScholarEducationDto addScholarEducation(ScholarEducationDto scholarEducationDto, Long scholarId) {
        ScholarEducation scholarEducation = mapToEntity(scholarEducationDto);
        Application application = applicationRepository.findById(scholarId).orElseThrow(() -> new UserDetailsNotFoundException("Applicant Not found"));
        /*scholarEducation.setScholar(scholar);*/ //(i have replaced scholar with application)
        scholarEducation.setApplication(application);
        scholarEducation.setForm(scholarEducationDto.getForm());
        scholarEducation.setTerm(scholarEducationDto.getTerm());
        scholarEducation.setOpening_date(scholarEducationDto.getOpening_date());
//        scholarEducation.setSchoolName(scholarEducationDto.getSchoolName());
        scholarEducation.setOpeningGrade(scholarEducationDto.getOpeningGrade());
        scholarEducation.setMidTermGrade(scholarEducationDto.getMidTermGrade());
        scholarEducation.setClosingGrade(scholarEducationDto.getClosingGrade());
//        scholarEducation.setApplication(scholarEducationDto.getApplication());
        ScholarEducation newScholar = scholarEducationRepo.save(scholarEducation);
        return mapToDto(newScholar);
    }

    @Override
    public List<ScholarEducationDto> findByApplicationId(Long applicationId) {
        List<ScholarEducation> scholarEducations = scholarEducationRepo.findByApplicationId(applicationId);
        return scholarEducations.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ScholarEducationDto updateScholarEducation(Long scholarEducationId, ScholarEducationDto updatedScholarEducationDto) {
        ScholarEducation existingScholarEducation = scholarEducationRepo.findById(scholarEducationId)
                .orElseThrow(()-> new NoResourceFoundException("scholar Education not found"));
        //replacing or updating or whatever
        existingScholarEducation.setForm(updatedScholarEducationDto.getForm());
        existingScholarEducation.setTerm(updatedScholarEducationDto.getTerm());
        existingScholarEducation.setOpening_date(updatedScholarEducationDto.getOpening_date());
//        existingScholarEducation.setSchoolName(updatedScholarEducationDto.getSchoolName());
        existingScholarEducation.setOpeningGrade(updatedScholarEducationDto.getOpeningGrade());
        existingScholarEducation.setMidTermGrade(updatedScholarEducationDto.getMidTermGrade());
        existingScholarEducation.setClosingGrade(updatedScholarEducationDto.getClosingGrade());

        //save the changed entity
        ScholarEducation updatedSchoolEducation = scholarEducationRepo.save(existingScholarEducation);

        return mapToDto(updatedSchoolEducation);
    }

    @Override
    public void deleteScholarEducationById(Long scholarEducationId) {
        if (!scholarEducationRepo.existsById(scholarEducationId)) {
            throw new NoResourceFoundException("No scholar education found for ID: " + scholarEducationId);
        }
        scholarEducationRepo.deleteById(scholarEducationId);
    }


    private ScholarEducationDto mapToDto(ScholarEducation scholarEducation) {
        ScholarEducationDto scholarEducationDto = new ScholarEducationDto();
        scholarEducationDto.setId(scholarEducation.getId());
        scholarEducationDto.setForm(scholarEducation.getForm());
        scholarEducationDto.setTerm(scholarEducation.getTerm());
        scholarEducationDto.setOpening_date(scholarEducation.getOpening_date());
//        scholarEducationDto.setSchoolName(scholarEducation.getSchoolName());
        scholarEducationDto.setOpeningGrade(scholarEducation.getOpeningGrade());
        scholarEducationDto.setMidTermGrade(scholarEducation.getMidTermGrade());
        scholarEducationDto.setClosingGrade(scholarEducation.getClosingGrade());
        scholarEducationDto.setApplication(scholarEducation.getApplication());
        return scholarEducationDto;

    }

    private ScholarEducation mapToEntity(ScholarEducationDto scholarEducationDto) {
        ScholarEducation scholarEducation = new ScholarEducation();
        scholarEducation.setId(scholarEducationDto.getId());
        scholarEducation.setForm(scholarEducationDto.getForm());
        scholarEducation.setTerm(scholarEducationDto.getTerm());
        scholarEducation.setOpening_date(scholarEducationDto.getOpening_date());
//        scholarEducation.setSchoolName(scholarEducationDto.getSchoolName());
        scholarEducation.setOpeningGrade(scholarEducationDto.getOpeningGrade());
        scholarEducation.setMidTermGrade(scholarEducationDto.getMidTermGrade());
        scholarEducation.setClosingGrade(scholarEducationDto.getClosingGrade());
        scholarEducation.setApplication(scholarEducationDto.getApplication());
        return scholarEducation;
    }
}
