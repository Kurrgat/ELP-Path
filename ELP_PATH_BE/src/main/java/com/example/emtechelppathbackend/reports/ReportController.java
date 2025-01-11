package com.example.emtechelppathbackend.reports;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/scholar-report")
    public ResponseEntity<?> generateScholarReport(@RequestParam(name = "year", required = false) String year,
                                                @RequestParam(name = "gender", required = false)String gender,
                                                @RequestParam(name="branch",required = false) String branch,
                                                @RequestParam(name = "scholarType", required = false) String scholarType,
                                                   @RequestParam(name = "ignoreCondition", required = false, defaultValue = "false") Boolean ignoreCondition
                                                   )  {
        try {
            byte[] report = reportService.generateScholarReport(year,gender,branch,scholarType,ignoreCondition);


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

    @GetMapping(value = "/generate-users-Report")
    public ResponseEntity<?> createPdf() throws IOException, JRException {
        try {
            byte[] report = reportService.generateReport();

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

    @GetMapping( "/generate-events-report")
    public ResponseEntity<?> generateEventsReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) throws IOException, JRException {
        try {
            byte[] report = reportService.generateEventsReport(startDate, endDate);

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

    @GetMapping( "/generate-chapter-report")
    public ResponseEntity<?> generateChapterReport() throws IOException, JRException {
        try {
            byte[] report = reportService.generateChapterReport();

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
    @GetMapping( "/generate-job-opportunity-report")
    public ResponseEntity<?> generateJobOpportunityReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) throws IOException, JRException {
        try {
            byte[] report = reportService.generateJobOpportunityReport(startDate,endDate);

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
    @GetMapping( "/generate-report-on-hubs")
    public ResponseEntity<?> generateHubsReport() throws IOException, JRException {
        try {
            byte[] report = reportService.generateHubsReport();

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
}
