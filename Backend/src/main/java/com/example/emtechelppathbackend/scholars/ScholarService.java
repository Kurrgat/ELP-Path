package com.example.emtechelppathbackend.scholars;

import com.example.emtechelppathbackend.application.wtf_application.Gender;
import com.example.emtechelppathbackend.education.Institution;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import net.sf.jasperreports.engine.JRException;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
@Service
public interface ScholarService {
    Scholar addNewScholar(Scholar scholar);
    Scholar getScholarById(Long id);
    Optional<Scholar> updateScholarById(Long id, Scholar scholar);
    void deleteScholarById(Long id);
//    List<Scholar> displayScholars() throws Exception;
    long getTotalScholars();
    Map<Integer, Long> getTotalScholarsByYear();
    Integer getTotalFemaleScholars();
    Map<String, Integer> getTotalMaleScholarsPerYear();
    Map<String, Integer> getTotalFemaleScholarsPerYear();
    Integer getTotalMaleScholars();
    Integer getTotalLocalScholars();
    Integer getTotalGlobalScholars();
    Integer getTotalMCFScholars();
    Integer getTotalDFIDScholars();
    Integer getTotalUSAIDScholars();
    Integer getTotalHSBCScholars();
    Integer getTotalKFWScholars();
    Integer getTotalVitolScholars();
    Integer getTotalCHAScholars();
    Integer getTotalRAOScholars();
    Integer getTotalDCLScholars();
    Integer getTotalMFIScholars();
    Integer getTotalGGScholars();
    Integer getTotalSSScholars();
    Integer getTotalEGFScholars();
    Integer getTotalFJScholars();
    Integer getTotalEBLEScholars();
    Integer getTotalEGFIScholars();
    Integer getTotalASScholars();
    Integer getTotalTH009Scholars();
    Integer getTotalMMFScholars();
    Integer getTotalUHUKScholars();
    Integer getTotalFRScholars();
    Integer getTotalDPMScholars();
    Integer getTotalEVTScholars();
    Integer getTotalDonors();

    CustomResponse<Set<ScholarDTO_2>> filterScholars(String gender, String donor, String branch, String institution, String scholarCategory, String scholarType);
    Set<Scholar> getScholarByScholarCategory(ScholarCategories category) throws NoResourceFoundException;
    Set<Scholar> getScholarsByGender(Gender gender) throws NoResourceFoundException;
    Set<Scholar> getScholarsByScholarType(ScholarType type) throws NoResourceFoundException;
    Set<ScholarDTO> getScholarsByBranchId(Long branchId) throws NoResourceFoundException;
    Set<ScholarDTO> getScholarsByInstitutionId(Long institutionId) throws NoResourceFoundException;
    byte[] generateReport() throws JRException, IOException;
    CustomResponse<?> handleBulkUpload(MultipartFile file);

    CustomResponse<?> getGenderStats();
    CustomResponse<?> getScholarStats(String year, Boolean ignoreCondition);
    CustomResponse<?> getDonorStats();

   

    CustomResponse<?> getGlobalScholars(String year);

    CustomResponse<?> getLocalScholars(String year);
    CustomResponse<?>getTvetScholars(String year);

    Slice<Scholar> displayScholars(Pageable pageable);

    CustomResponse<?> getAllScholarsPerYear(String year);

    CustomResponse<?> getDonorsPerYear(String year);

    CustomResponse<?> getRegionalScholars(String year);

    CustomResponse<?> getScholarDescription(Long id);

    byte[] generateReportPerYear(String year)throws JRException, IOException;

//    byte[] generateAnnualLocalReport(String year);

//    CustomResponse<?> getFilteredScholars();
}

