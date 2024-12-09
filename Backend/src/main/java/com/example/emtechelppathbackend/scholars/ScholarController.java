package com.example.emtechelppathbackend.scholars;

import com.example.emtechelppathbackend.application.wtf_application.Gender;
import com.example.emtechelppathbackend.customizedimports.ApiResponse;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.school.SchoolService;
import com.example.emtechelppathbackend.responserecords.ResponseRecord;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.engine.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import java.text.DateFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scholars")
public class ScholarController {
    @Autowired
    private final ScholarService scholarService;
    private final SchoolService schoolService;
    private final ModelMapper modelMapper;

    Map<String, Integer> metric = new HashMap<>();

    @PostMapping("/add-new-scholar")
    public ResponseEntity<ScholarDTO> addNewScholar(@RequestBody ScholarDTO scholarDTO) {

        //convert Dto to entity
        Scholar scholarRequest = modelMapper.map(scholarDTO, Scholar.class);

        Scholar scholar = scholarService.addNewScholar(scholarRequest);

        //convert entity to DTO
        ScholarDTO scholarResponse = modelMapper.map(scholar, ScholarDTO.class);

        return new ResponseEntity<ScholarDTO>(scholarResponse, HttpStatus.CREATED);
    }

    @GetMapping("/display-scholars/{id}")
    public ResponseEntity<ScholarDTO> displayScholarDetails(@PathVariable(name = "id") Long id) {
        Scholar scholar = scholarService.getScholarById(id);

        //convert to DTO
        ScholarDTO scholarResponse = modelMapper.map(scholar, ScholarDTO.class);

        return ResponseEntity.ok().body(scholarResponse);
    }

    @PutMapping("/update-scholar/{id}")
    public ResponseEntity<ScholarDTO> updateScholar(@PathVariable long id,
                                                    @RequestBody ScholarDTO scholarDTO) {

        //convert DTO to entity
        Scholar scholarRequest = modelMapper.map(scholarDTO, Scholar.class);

        Optional<Scholar> scholar = scholarService.updateScholarById(id, scholarRequest);

        //entity to DTO
        ScholarDTO scholarResponse = modelMapper.map(scholar, ScholarDTO.class);
        return ResponseEntity.ok().body(scholarResponse);
    }

    @DeleteMapping("/delete-scholar/{id}")
    public ResponseEntity<ApiResponse> deleteScholar(@PathVariable(name = "id") Long id) {
        scholarService.deleteScholarById(id);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Scholar deleted successfully", HttpStatus.OK);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/display-scholars")
    public List<ScholarDTO> displayScholars(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "25") int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);

        Slice<Scholar> scholarSlice = scholarService.displayScholars(pageable);

        return scholarSlice.getContent()
                .stream()
                .map(scholar -> modelMapper.map(scholar, ScholarDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/filterScholars")
    public ResponseEntity<?> filterScholars(
            @RequestParam(required = false)
            String gender,

            @RequestParam(required = false)
            String donor,

            @RequestParam(required = false)
            String branch,

            @RequestParam(required = false)
            String institution,

            @RequestParam(required = false)
            String scholarCategory,

            @RequestParam(required = false)
            String scholarType
    ) {
        try {
            var scholars = scholarService.filterScholars(gender, donor, branch, scholarCategory, scholarType, institution);
            return ResponseEntity.status(scholars.getStatusCode()).body(scholars);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(new ResponseRecord(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("{branchId}/display-scholars-by-branch")
    public ResponseEntity<?> displayScholarsByBranchId(@PathVariable Long branchId) {
        try {
            Set<ScholarDTO> scholars = scholarService.getScholarsByBranchId(branchId);
            return ResponseEntity.ok(scholars);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{institutionId}/display-scholars-by-institution")
    public ResponseEntity<?> getScholarsByInstitution(@PathVariable Long institutionId) {
        try {
            Set<ScholarDTO> scholars = scholarService.getScholarsByInstitutionId(institutionId);
            return ResponseEntity.ok(scholars);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{category}/display-scholars-by-category")
    public ResponseEntity<?> getScholarsByScholarCategory(@PathVariable("category") ScholarCategories category) {
        try {
            Set<Scholar> scholars = scholarService.getScholarByScholarCategory(category);
            return ResponseEntity.ok(scholars);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{gender}/display-scholars-by-gender")
    public ResponseEntity<?> getScholarsByGender(@PathVariable("gender") Gender gender) {
        try {
            Set<Scholar> scholars = scholarService.getScholarsByGender(gender);
            return ResponseEntity.ok(scholars);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{type}/display-scholars-by-type")
    public ResponseEntity<?> getScholarsByType(@PathVariable("type") ScholarType type) {
        try {
            Set<Scholar> scholars = scholarService.getScholarsByScholarType(type);
            return ResponseEntity.ok(scholars);
        } catch (NoResourceFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/generateReport")
    public ResponseEntity<?> createPdf() throws IOException, JRException {
        try {
            byte[] report = scholarService.generateReport();

            HttpHeaders headers = new HttpHeaders();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh.mm.ss");
            String currentDateTime = dateFormat.format(new Date());

            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "students" + currentDateTime+".pdf");

            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("bulk-upload")
    public ResponseEntity<?> handleBulkUpload(MultipartFile file) {
        try {
            var response = scholarService.handleBulkUpload(file);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }

    }

    //number metrics to be displayed on the landing page
    @GetMapping("/count-scholars-by-year")
    public Map<Integer, Long> getTotalScholarsByYear() {
        return scholarService.getTotalScholarsByYear();
    }

    @GetMapping("/get-all-statistics")
    public Map<String, Integer> getAllStatistics(){
        //total female scholars
        int totalFemale = scholarService.getTotalFemaleScholars();
        metric.put("Total female scholars", totalFemale);

        Map<String, Integer> totalFemaleScholarsPerYear = scholarService.getTotalFemaleScholarsPerYear();

        totalFemaleScholarsPerYear.forEach((key, value) -> metric.put("Total female scholars for the year " + key, value));

        //total male scholars
        int totalMale = scholarService.getTotalMaleScholars();
        metric.put("Total male scholars", totalMale );

        Map<String, Integer> totalMaleScholarsPerYear = scholarService.getTotalMaleScholarsPerYear();

        totalMaleScholarsPerYear.forEach((key, value) -> metric.put("Total male scholars for the year " + key, value));

        int totalLocal = scholarService.getTotalLocalScholars();
        metric.put("Local", totalLocal);

        int totalGlobal = scholarService.getTotalGlobalScholars();
        metric.put("Global", totalGlobal);

        int totalMCF = scholarService.getTotalMCFScholars();
        metric.put("MCF", totalMCF);

        int totalDFID = scholarService.getTotalDFIDScholars();
        metric.put("DFID", totalDFID);

        int totalUSAID = scholarService.getTotalUSAIDScholars();
        metric.put("USAID", totalUSAID);

        int totalHSBC = scholarService.getTotalHSBCScholars();
        metric.put("HSBC", totalHSBC);

        int totalKFW = scholarService.getTotalKFWScholars();
        metric.put("KFW", totalKFW);

        int totalVitol = scholarService.getTotalVitolScholars();
        metric.put("VITOL", totalVitol);

        int totalCHA = scholarService.getTotalCHAScholars();
        metric.put("CHA", totalCHA);

        int totalRAO = scholarService.getTotalRAOScholars();
        metric.put("RAO", totalRAO);

        int totalDCL = scholarService.getTotalDCLScholars();
        metric.put("DCL", totalDCL);

        int totalMFI = scholarService.getTotalMFIScholars();
        metric.put("MFI", totalMFI);

        int totalGG = scholarService.getTotalGGScholars();
        metric.put("GG", totalGG);

        int totalSS = scholarService.getTotalSSScholars();
        metric.put("SS", totalSS);

        int totalEGF = scholarService.getTotalEGFScholars();
        metric.put("EGF", totalEGF);

        int totalFJ = scholarService.getTotalFJScholars();
        metric.put("FJ", totalFJ);

        int totalEBLE = scholarService.getTotalEBLEScholars();
        metric.put("EBLE", totalEBLE);

        int totalEGFI = scholarService.getTotalEGFIScholars();
        metric.put("EGFI", totalEGFI);

        int totalAS = scholarService.getTotalASScholars();
        metric.put("AS", totalAS);

        int totalTH009 = scholarService.getTotalTH009Scholars();
        metric.put("TH009", totalTH009);

        int totalMMF = scholarService.getTotalMMFScholars();
        metric.put("MMF", totalMMF);

        int totalUHUK = scholarService.getTotalUHUKScholars();
        metric.put("UHUK",  totalUHUK);

        int totalFR = scholarService.getTotalFRScholars();
        metric.put("FR", totalFR);

        int totalDPM = scholarService.getTotalDPMScholars();
        metric.put("DPM", totalDPM);

        int totalEVT = scholarService.getTotalEVTScholars();
        metric.put("EVT", totalEVT);

        int totalDonors = scholarService.getTotalDonors();
        metric.put("Total donors", totalDonors);

        return metric;
    }

    @GetMapping("/gender-stats")
    ResponseEntity<?> getGenderStats() {
        var response = scholarService.getGenderStats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/scholar-stats/")
    ResponseEntity<?> getScholarStats(
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "ignoreCondition", required = false, defaultValue = "false") Boolean ignoreCondition
    ) {
        var response = scholarService.getScholarStats(year, ignoreCondition);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/donor-stats")
    ResponseEntity<?> getDonorStats() {
        var response = scholarService.getDonorStats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/{year}/donor-view")
    ResponseEntity<?> getDonors(@RequestParam String year) {
        var response = scholarService.getDonorsPerYear(year);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/global/scholars-view/{year}")
    ResponseEntity<?> getGlobalScholars(@PathVariable String year) {
        var response = scholarService.getGlobalScholars(year);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/local/scholars-view/{year}")
    ResponseEntity<?> getLocalScholars(@RequestParam String year) {
        var response = scholarService.getLocalScholars(year);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/tvet/scholars-view/{year}")
    ResponseEntity<?> getTvetScholars(@RequestParam String year) {
        var response = scholarService.getTvetScholars(year);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{year}/scholars-view")
    ResponseEntity<?> getScholarsPerYear(@RequestParam String year) {
        var response = scholarService.getAllScholarsPerYear(year);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/{year}/regional/scholars-view")
    ResponseEntity<?> getRegionalScholars(@RequestParam String year) {
        var response = scholarService.getRegionalScholars(year);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/scholar-description/{userId}")
    ResponseEntity<?> getScholarDescription(@RequestParam Long id) {
        var response = scholarService.getScholarDescription(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping(value = "/generateReport/{year}")
    public ResponseEntity<?> createAnnualReport(@PathVariable String year) throws IOException, JRException {
        try {
            byte[] report = scholarService.generateReportPerYear(year);

            HttpHeaders headers = new HttpHeaders();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh.mm.ss");
            String currentDateTime = dateFormat.format(new Date());

            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "students" + currentDateTime+".pdf");

            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


//    @GetMapping(value = "/generate-annual/local/report/{year}")
//    public ResponseEntity<?> createLocalAnnualReport(@PathVariable String year) throws IOException, JRException {
//        try {
//            byte[] report = scholarService.generateAnnualLocalReport(year);
//
//            HttpHeaders headers = new HttpHeaders();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh.mm.ss");
//            String currentDateTime = dateFormat.format(new Date());
//
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("attachment", "students" + currentDateTime+".pdf");
//
//            return new ResponseEntity<>(report, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

}

