package com.example.emtechelppathbackend.scholars;

import com.example.emtechelppathbackend.Career.Career;
import com.example.emtechelppathbackend.Career.CareerDto;
import com.example.emtechelppathbackend.Career.CareerRepo;
import com.example.emtechelppathbackend.bio.Bio;
import com.example.emtechelppathbackend.bio.BioDto;
import com.example.emtechelppathbackend.bio.BioRepository;
import com.example.emtechelppathbackend.education.Education;
import com.example.emtechelppathbackend.education.EducationDto;
import com.example.emtechelppathbackend.education.EducationRepo;
import com.example.emtechelppathbackend.school.SchoolRepository;
import com.example.emtechelppathbackend.application.wtf_application.Gender;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.exceptions.ResourceNotFoundException;
import com.example.emtechelppathbackend.security.user.*;
import com.example.emtechelppathbackend.skills.Skills;
import com.example.emtechelppathbackend.skills.SkillsDto;
import com.example.emtechelppathbackend.skills.SkillsRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScholarServiceImpl implements ScholarService {
    private final ScholarRepo scholarRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;
    private final EducationRepo educationRepo;
    private  final CareerRepo careerRepo;
    private  final SkillsRepository skillsRepository;
    private final BioRepository bioRepo;
    private final UsersRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public Scholar addNewScholar(Scholar scholar) {
        return scholarRepository.save(scholar);
    }

    @Override
    public Scholar getScholarById(Long id) {
        Optional<Scholar> scholar = scholarRepository.findById(id);
        if (scholar.isPresent()) {
            return scholar.get();
        } else {
            throw new ResourceNotFoundException("Scholar", "id", id);
        }
    }

    @Override
    public Optional<Scholar> updateScholarById(Long id, Scholar schol) {
        Optional<Scholar> scholar = scholarRepository.findById(id);
        if (scholar.isPresent()) {
            Scholar existingScholar = scholar.get();
            existingScholar.setScholarCode(schol.getScholarCode());
            existingScholar.setPfNumber(schol.getPfNumber());
            existingScholar.setScholarFirstName(schol.getScholarFirstName());
            existingScholar.setScholarLastName(schol.getScholarLastName());
            existingScholar.setScholarDOB(schol.getScholarDOB());
            existingScholar.setGender(schol.getGender());
            existingScholar.setBranch(schol.getBranch());
            existingScholar.setScholarCategory(schol.getScholarCategory());
            existingScholar.setYearOfJoiningHighSchoolProgram(schol.getYearOfJoiningHighSchoolProgram());
            existingScholar.setYearOfJoiningTertiaryProgram(schol.getYearOfJoiningTertiaryProgram());
            existingScholar.setSchool(schol.getSchool());
            existingScholar.setInstitution(schol.getInstitution());
            existingScholar.setDonor(schol.getDonor());
            // existingScholar.setHomeCounty(schol.getHomeCounty());
            existingScholar.setScholarType(schol.getScholarType());
            existingScholar.setCountryOfOrigin(schol.getCountryOfOrigin());


            //user is no longer a property in the scholar entity
            //existingScholar.setUser(schol.getUser());

            //existingScholar.setUser(schol.getUser());

            // Save the updated Scholar entity
            scholarRepository.save(existingScholar);

            return Optional.of(existingScholar);
        } else {
            // Handle the case where scholar is not present
            // Return an empty Optional, throw an exception, or handle it in another appropriate way
            return Optional.empty();
        }
    }

    @Override
    public void deleteScholarById(Long id) {
        Scholar scholar = scholarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scholar", "id", id));
        scholarRepository.delete(scholar);
    }

    @Override
    public Slice<Scholar> displayScholars(Pageable pageable) {
        return scholarRepository.findAll(pageable);
    }

    @Override
    public CustomResponse<?> getAllScholarsPerYear(String year, Boolean ignoreCondition) {
        CustomResponse<List<Scholar>> response = new CustomResponse<>();
        try {
            List<Scholar> allList = scholarRepository.getAllScholarsPerYear(year, ignoreCondition);

            if (allList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(allList);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getDonorsPerYear(String year) {
        CustomResponse<List<DonorInterface>> response = new CustomResponse<>();

        try {
            List<DonorInterface> donorsList = scholarRepository.getDonorsPerYear(year);

            if (donorsList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(donorsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getRegionalScholars(String year, Boolean ignoreCondition) {
        CustomResponse<List<Scholar>> response = new CustomResponse<>();

        try {
            List<Scholar> regionalList = scholarRepository.getRegionalScholars(year, ignoreCondition);

            if (regionalList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(regionalList);
            }
        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getMales( String year, Boolean ignoreCondition) {
        CustomResponse<List<ScholarRepo.MaleScholarsInterface>> response = new CustomResponse<>();

        try {
            List<ScholarRepo.MaleScholarsInterface> maleList = scholarRepository.getMaleScholars(year, ignoreCondition);

            if (maleList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(maleList);
            }
        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getFemales(String year, Boolean ignoreCondition) {
        CustomResponse<List<ScholarRepo.FemaleScholarsInterface>> response = new CustomResponse<>();

        try {
            List<ScholarRepo.FemaleScholarsInterface> femaleList = scholarRepository.getFemaleScholars(year, ignoreCondition);

            if (femaleList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(femaleList);
            }
        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<UserCountDto> getUserStats() {
        CustomResponse<UserCountDto> response = new CustomResponse<>();

        try {
            ScholarRepo.RegisteredAndUnregisteredCount counts = scholarRepository.getUserCount();

            if (counts == null) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                UserCountDto userCountDto = new UserCountDto(counts.getRegisteredUsers(), counts.getUnregisteredUsers());
                response.setSuccess(true);
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(userCountDto);
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    @Override
    public CustomResponse<?> getScholarDescription(Long userId) {
        CustomResponse<Map<String, Object>> response = new CustomResponse<>();
        Map<String, Object> description = new LinkedHashMap<>();
        try {
            List<Bio> bios = Collections.singletonList(bioRepo.findBioByUserId(userId));
            List<Education> educations = educationRepo.findByUserId(userId);
            List<Career> careers = careerRepo.findByUserId(userId);
            List<Skills>skill=skillsRepository.findByUserId(userId);

            List<BioDto>bioDTOs=convertToBioDTOs(bios);
            List<EducationDto> educationDTOs = convertToEducationDTOs(educations);
            List<CareerDto> careerDTOs = convertToCareerDTOs(careers);
            List<SkillsDto>skillsDTOs=convertToSkillsDTOS(skill);

            description.put("bio", bioDTOs);
            description.put("education", educationDTOs);
            description.put("career", careerDTOs);
            description.put("skill", skillsDTOs);

            response.setMessage("Scholar profile retrieved successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(description);
        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    private List<BioDto> convertToBioDTOs(List<Bio> bios) {
        ModelMapper modelMapper = new ModelMapper();
        return bios.stream()
                .map(bio -> modelMapper.map(bio, BioDto.class))
                .collect(Collectors.toList());
    }

    private List<SkillsDto> convertToSkillsDTOS(List<Skills> skill) {
        ModelMapper modelMapper = new ModelMapper();
        return skill.stream()
                .map( skills-> modelMapper.map(skills, SkillsDto.class))
                .collect(Collectors.toList());
    }

    private List<CareerDto> convertToCareerDTOs(List<Career> careers) {
        ModelMapper modelMapper = new ModelMapper();
        return careers.stream()
                .map(career -> modelMapper.map(career, CareerDto.class))
                .collect(Collectors.toList());
    }

    private List<EducationDto> convertToEducationDTOs(List<Education> educations) {
        ModelMapper modelMapper = new ModelMapper();
        return educations.stream()
                .map(education -> modelMapper.map(education, EducationDto.class))
                .collect(Collectors.toList());
    }



    @Override
    public CustomResponse<Set<ScholarDTO_2>> filterScholars(String gender, String donor, String branch, String institution, String scholarCategory, String scholarType) {
        CustomResponse<Set<ScholarDTO_2>> response = new CustomResponse<>();

        try {
            Set<ScholarDTO_2> scholars = scholarRepository.filterScholars(gender, donor, branch, scholarCategory, scholarType, institution);

            if (scholars.isEmpty()) {
                response.setMessage("There are no scholars who match the specified criteria");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            } else {
                response.setMessage("These are the scholars who match the specified criteria");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(scholars);
            }
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public long getTotalScholars() {
        return scholarRepository.getTotalScholars();
    }

    @Override
    public Map<Integer, Long> getTotalScholarsByYear() {
        String queryString = "SELECT YEAR(dateOf) as year, COUNT(*) as count " +
                "FROM Elimu" +
                "GROUP BY YEAR(dateOfApplication)";
        TypedQuery<Object[]> query = entityManager.createQuery(queryString, Object[].class);
        List<Object[]> rows = query.getResultList();

        Map<Integer, Long> results = new HashMap<>();
        for (Object[] row : rows) {
            int year = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            results.put(year, count);
        }

        return results;
    }

    @Override
    public Set<Scholar> getScholarByScholarCategory(ScholarCategories category) throws NoResourceFoundException {
        Set<Scholar> scholars = scholarRepository.findScholarsByScholarCategory(category);

        if (scholars.isEmpty()) {
            throw new NoResourceFoundException("There are no scholars in this category");
        } else {
            return scholarRepository.findAll().stream()
                    .filter(scholar -> scholar.getScholarCategory() == category)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<Scholar> getScholarsByGender(Gender gender) throws NoResourceFoundException {
        Set<Scholar> scholars = scholarRepository.findScholarsByGender(gender);

        if (scholars.isEmpty()) {
            throw new NoResourceFoundException("There are no scholars in this category");
        } else {
            return scholarRepository.findAll().stream()
                    .filter(scholar -> scholar.getGender() == gender)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<Scholar> getScholarsByScholarType(ScholarType type) throws NoResourceFoundException {
        Set<Scholar> scholars = scholarRepository.findScholarsByScholarType(type);

        if (scholars.isEmpty()) {
            throw new NoResourceFoundException("There are no scholars of this type");
        } else {
            return scholarRepository.findAll().stream()
                    .filter(scholar -> scholar.getScholarType() == type)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<ScholarDTO> getScholarsByBranchId(Long branchId) throws NoResourceFoundException {
        Set<Scholar> possibleScholars = scholarRepository.findScholarsByBranchId(branchId);
        if (possibleScholars.isEmpty()) {
            throw new NoResourceFoundException("This Branch has no scholars associated with it");
        } else {
            return possibleScholars.stream().map(this::convertScholarToDto).collect(Collectors.toSet());
        }
    }

    @Override
    public Set<ScholarDTO> getScholarsByInstitutionId(Long institutionId) throws NoResourceFoundException {
        Set<Scholar> possibleScholars = scholarRepository.findScholarsByInstitutionId(institutionId);
        if (possibleScholars.isEmpty()) {
            throw new NoResourceFoundException("There are no scholars associated with this institution");
        } else {
            return possibleScholars.stream().map(this::convertScholarToDto).collect(Collectors.toSet());
        }
    }

    @Transactional(dontRollbackOn = DataIntegrityViolationException.class)
    @Override
    public CustomResponse<Object> handleBulkUpload(MultipartFile file) {
        CustomResponse<Object> response = new CustomResponse<>();
        try {
            String sqlStatements = BulkUpload.convertCSVToSQL(file.getInputStream());
            String[] statements = sqlStatements.split(";");
            boolean flag = false;
            int counter = 0;
//
            for (String statement : statements) {
                // Remove leading and trailing whitespaces
                String trimmedStatement = statement.trim();

                if (!trimmedStatement.isEmpty()) {
//                    try {
                    entityManager.createNativeQuery(statement).executeUpdate();
//                          System.out.println(status);
                    counter++;
                    flag = true;
//                    } catch (DataIntegrityViolationException e) {
//                        // Ignore the exception to prevent rollback
//                        System.out.println(e.getMessage());
//                    } catch (Exception e) {
//                        if (e.getMessage().contains("Duplicate")){
//                            response.setPayload(e.getMessage());
//                        }
//                    }
                }
            }

            response.setSuccess(flag);
            response.setMessage("Successfully uploaded " + counter + " records");
            response.setStatusCode(HttpStatus.OK.value());

        } catch(Exception e) {
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
    }
            return response;

}





//    public byte[] generateReport() throws JRException, IOException {
//        List<UsersDto1> result = userRepository.findAll().stream()
//                .map(users -> modelMapper.map(users, UsersDto1.class))
//                .toList();
//
//        try {
//            //get file and compile it
//            File file = ResourceUtils.getFile("classpath:users.jrxml");
//            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//
//            //get datasource
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
//
//            //add parameters
//            Map<String, Object> parameters = new HashMap<>();
//
//            parameters.put("createdBy", "Michael");
//
//            //fill report
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//            System.out.println("Report generated successfully");
//
//            //export
//            return JasperExportManager.exportReportToPdf(jasperPrint);
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException("Unable to generate report");
//        }
//    }

    @Override
    public Integer getTotalFemaleScholars(){
        return scholarRepository.getTotalFemaleScholars();
    }
    public Integer getTotalMaleScholars(){
        return scholarRepository.getTotalMaleScholars();
    }

    @Override
    public Integer getTotalLocalScholars(){
        return scholarRepository.getTotalLocalScholars();
    }
    @Override
    public Integer getTotalGlobalScholars(){
        return scholarRepository.getTotalGlobalScholars();
    }
    @Override
    public Integer getTotalMCFScholars() { return scholarRepository.getTotalMCFScholars(); }
    @Override
    public Integer getTotalDFIDScholars() { return scholarRepository.getTotalDFIDScholars(); }
    @Override
    public Integer getTotalKFWScholars() { return scholarRepository.getTotalKFWScholars(); }
    @Override
    public Integer getTotalUSAIDScholars() { return scholarRepository.getTotalUSAIDScholars(); }

    @Override
    public Integer getTotalVitolScholars() { return scholarRepository.getTotalVitolScholars(); }
    @Override
    public Integer getTotalCHAScholars() { return scholarRepository.getTotalCHAScholars(); }
    @Override
    public Integer getTotalRAOScholars() { return scholarRepository.getTotalRAOScholars();}
    @Override
    public Integer getTotalDCLScholars() { return scholarRepository.getTotalDCLScholars();}
    @Override
    public Integer getTotalMFIScholars(){ return scholarRepository.getTotalMFIScholars();}
    @Override
    public Integer getTotalGGScholars(){ return scholarRepository.getTotalGGScholars();}
    @Override
    public Integer getTotalSSScholars(){ return scholarRepository.getTotalSSScholars();}
    @Override
    public Integer getTotalEGFScholars(){ return scholarRepository.getTotalEGFScholars();}
    @Override
    public Integer getTotalFJScholars(){ return scholarRepository.getTotalFJScholars();}
    @Override
    public Integer getTotalEBLEScholars(){ return scholarRepository.getTotalEBLEScholars();}
    @Override
    public Integer getTotalEGFIScholars(){ return scholarRepository.getTotalEGFIScholars();}
    @Override
    public Integer getTotalASScholars(){ return scholarRepository.getTotalASScholars();}
    @Override
    public Integer getTotalTH009Scholars(){ return scholarRepository.getTotalTH009Scholars();}
    @Override
    public Integer getTotalMMFScholars(){ return scholarRepository.getTotalMMFScholars();}
    @Override
    public Integer getTotalUHUKScholars(){ return scholarRepository.getTotalUHUKScholars();}
    @Override
    public Integer getTotalFRScholars(){ return scholarRepository.getTotalFRScholars();}

    @Override
    public Integer getTotalHSBCScholars(){ return scholarRepository.getTotalHSBCScholars();}
    @Override
    public Integer getTotalDPMScholars(){ return scholarRepository.getTotalDPMScholars();}
    @Override
    public Integer getTotalEVTScholars(){ return scholarRepository.getTotalEVTScholars();}

    @Override
    public Integer getTotalDonors(){ return scholarRepository.getTotalDonors();}

    @Override
    public Map<String, Integer> getTotalMaleScholarsPerYear() {
        List<Object[]> result = scholarRepository.getTotalMaleScholarsPerYear();

        // Convert the result to a Map
        Map<String, Integer> resultMap = result.stream()
                .collect(Collectors.toMap(
                        array -> (String) array[0].toString(),
                        array -> {
                            Long longValue = (Long) array[1];
                            if(longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                                return longValue.intValue();
                            } else {
                                throw new IllegalStateException("long value out of index range");
                            }
                        }
                ));

        return resultMap;
    }

    @Override
    public Map<String, Integer> getTotalFemaleScholarsPerYear() {
        List<Object[]> result = scholarRepository.getTotalFemaleScholarsPerYear();

        // Convert the result to a Map
        Map<String, Integer> resultMap = result.stream()
                .collect(Collectors.toMap(
                        array -> array[0].toString(),
                        array -> {
                            Long longValue = (Long) array[1];
                            if(longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                                return longValue.intValue();
                            } else {
                                throw new IllegalStateException("long value out of index range");
                            }
                        }
                ));

        return resultMap;
    }

    @Override
    public CustomResponse<?> getGenderStats() {
        CustomResponse<Map<String, Object>> response = new CustomResponse<>();


        try {
            Map<String, Object> results = new HashMap<>();

            List<GenderCountInterface> males = scholarRepository.getMalesCount();
            List<GenderCountInterface> females = scholarRepository.getFemalesCount();

            results.put("male", males);
            results.put("female", females);

            response.setPayload(results);
            response.setMessage("Successful");
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setSuccess(false);
        }
        return response;
    }



    @Override
    public CustomResponse<?> getScholarStats(String year, Boolean ignoreCondition)  {
        CustomResponse<ScholarStatsInterface> response = new CustomResponse<>();

        try {
            ScholarStatsInterface scholarStats = scholarRepository.getScholarStats(year, ignoreCondition);

            if(scholarStats != null) {
                response.setPayload(scholarStats);
                response.setMessage("Successful");
            } else {
                response.setMessage("No stats found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<?> getDonorStats() {
        CustomResponse<Map<String, List<DonorInterface>>> response = new CustomResponse<>();

        try {
            List<DonorInterface> donorsList = scholarRepository.getDonors();

            if(donorsList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                Map<String, List<DonorInterface>> results = new HashMap<>();

                int totalDonors = donorsList.size();
                int half = totalDonors/2;

                List<DonorInterface> majorDonors = donorsList.subList(0, half);
                List<DonorInterface> minorDonors = donorsList.subList(half+1, totalDonors);

                if(totalDonors %2 != 0 ) {
                    majorDonors = donorsList.subList(0, half+1);
                    minorDonors = donorsList.subList(half + 1, totalDonors);
                }

                results.put("majorDonors", majorDonors);
                results.put("minorDonors", minorDonors);

                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(results);
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    private ScholarDTO convertScholarToDto(Scholar scholar){
        return modelMapper.map(scholar, ScholarDTO.class);
    }




//    @Override
//    public CustomResponse<?> getDonors() {
//        CustomResponse<List<DonorInterface>> response = new CustomResponse<>();
//
//        try {
//            List<DonorInterface> donorsList = scholarRepository.getDonors();
//
//            if (donorsList.isEmpty()) {
//                response.setSuccess(false);
//                response.setStatusCode(HttpStatus.NOT_FOUND.value());
//                response.setMessage("Records not found");
//            } else {
//                response.setMessage("Successful");
//                response.setStatusCode(HttpStatus.OK.value());
//                response.setPayload(donorsList);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setSuccess(false);
//            response.setMessage(e.getMessage());
//            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        }
//        return response;
//    }

    @Override
    public CustomResponse<?> getGlobalScholars(String year, Boolean ignoreCondition) {
        CustomResponse<List<Scholar>> response = new CustomResponse<>();
        try {
            List<Scholar> globalList = scholarRepository.getAllGlobalScholars(year, ignoreCondition);

            if (globalList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(globalList);
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getLocalScholars(String  year,  Boolean ignoreCondition) {
        CustomResponse<List<Scholar>> response = new CustomResponse<>();
        try {
            List<Scholar> localList = scholarRepository.getAllLocalScholars(year, ignoreCondition);
            System.out.println(localList);

            if (localList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(localList);
            }
        }catch (Exception e){

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return response;
    }

    @Override
    public CustomResponse<?> getTvetScholars(String year, Boolean ignoreCondition) {
        CustomResponse<List<Scholar>> response = new CustomResponse<>();
        try {
            List<Scholar> tvetList = scholarRepository.getAllTvetScholars(year, ignoreCondition);


            if (tvetList.isEmpty()) {
                response.setSuccess(false);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Records not found");
            } else {
                response.setMessage("Successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setPayload(tvetList);
            }
        }catch (Exception e){


            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }

        return response;
    }




    @Override
    public CustomResponse<?> getAlumni(String year, Boolean ignoreCondition) {
        CustomResponse<Scholar> response = new CustomResponse<>();

        try {
            Scholar scholarStats = scholarRepository.getAlumni(year, ignoreCondition);

            if(scholarStats != null) {
                response.setPayload(scholarStats);
                response.setMessage("Successful");
            } else {
                response.setMessage("Alumni not  found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getCurrentScholars(String year, Boolean ignoreCondition) {
        CustomResponse<Scholar> response = new CustomResponse<>();

        try {
            Scholar scholarStats = scholarRepository.getCurrentScholars(year, ignoreCondition);

            if(scholarStats != null) {
                response.setPayload(scholarStats);
                response.setMessage("Successful");
            } else {
                response.setMessage("Alumni not  found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
            }

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setSuccess(false);
        }
        return response;
    }


    public byte[] generateReport() throws JRException, IOException {
        List<UsersDto1> result = userRepository.findAll().stream()
                .map(users -> modelMapper.map(users, UsersDto1.class))
                .toList();

        try {
            //get InputStream for the JRXML file
            InputStream inputStream = new ClassPathResource("users.jrxml").getInputStream();

            //compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            //get datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);

            //add parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Michael");

            //fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            System.out.println("Report generated successfully");

            //export
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to generate report");
        }
    }

}





