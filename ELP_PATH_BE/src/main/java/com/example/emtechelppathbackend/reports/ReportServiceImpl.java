package com.example.emtechelppathbackend.reports;

import com.example.emtechelppathbackend.events.eventsv2.EventsRepov2;
import com.example.emtechelppathbackend.hubs.HubsRepo;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunity;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityRepository;
import com.example.emtechelppathbackend.jobopportunities.jobapplication.JobApplicationRepository;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import com.example.emtechelppathbackend.security.user.UsersDto1;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final ScholarRepo scholarRepo;
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final EventsRepov2 eventsRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final HubsRepo hubsRepo;


    @Override
    public byte[] generateScholarReport(String year, String gender, String branch, String scholarType,Boolean ignoreCondition) {
        List<ScholarRepo.ScholarReportInterface> scholars = scholarRepo.getScholars(year, gender, branch, scholarType,ignoreCondition).stream()
                .map(scholar -> modelMapper.map(scholar, ScholarRepo.ScholarReportInterface.class))
                .toList();


        try {
            // Get InputStream for the JRXML file
            InputStream inputStream = new ClassPathResource("scholars.jrxml").getInputStream();

            // Compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);


            // Get datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(scholars);


            // Add parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("year", year);
            parameters.put("gender", gender);
            parameters.put("branch", branch);
            parameters.put("scholarType", scholarType);
            // Assuming "createdBy" is a static parameter, you might still want to include it
            parameters.put("createdBy", "Langat");

            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            System.out.println("Report generated successfully");

            // Export
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to generate scholar report", e);
        }
    }

    public byte[] generateReport() {
        List<UsersDto1> result = usersRepository.findAll().stream()
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

    @Override
    public byte[] generateEventsReport(LocalDate startDate, LocalDate endDate) {
        List<EventsRepov2.EventInterface> result = eventsRepository.findByEventDateBetween(startDate, endDate).stream()
                .map(eventsV2 -> modelMapper.map(eventsV2, EventsRepov2.EventInterface.class))
                .toList();
        System.out.println(result);
        try {
            // get InputStream for the JRXML file
            InputStream inputStream = new ClassPathResource("events.jrxml").getInputStream();

            // compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // get datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);

            // add parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);

            // fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            System.out.println("Report generated successfully");

            // export
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to generate report");
        }
    }

    @Override
    public byte[] generateChapterReport() {
        List<EventsRepov2.ChapterReportInterface> result = eventsRepository.findChaptersReports().stream()
                .map(chapterV2 -> modelMapper.map(chapterV2, EventsRepov2.ChapterReportInterface.class))
                .toList();

        try {
            // get InputStream for the JRXML file
            InputStream inputStream = new ClassPathResource("chapters.jrxml").getInputStream();

            // compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // get datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);

            // add parameters
            Map<String, Object> parameters = new HashMap<>();


            // fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            System.out.println("Report generated successfully");

            // export
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to generate report");
        }
    }

    @Override
    public byte[] generateJobOpportunityReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<JobApplicationRepository.JobApplicationInterface> result = jobApplicationRepository.getJobOpportunityReport(startDate,endDate).stream()
                .map(jobApplication -> modelMapper.map(jobApplication, JobApplicationRepository.JobApplicationInterface.class))
                .toList();

        try {
            // get InputStream for the JRXML file
            InputStream inputStream = new ClassPathResource("opportunity.jrxml").getInputStream();

            // compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // get datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);

            // add parameters
            Map<String, Object> parameters = new HashMap<>();


            // fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            System.out.println("Report generated successfully");

            // export
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to generate report");
        }
    }

    @Override
    public byte[] generateHubsReport() {
        List<HubsRepo.HubReportInterface> result = hubsRepo.findHubsReports().stream()
                .map(hubv2 -> modelMapper.map(hubv2, HubsRepo.HubReportInterface.class))
                .toList();

        try {
            // get InputStream for the JRXML file
            InputStream inputStream = new ClassPathResource("hubs.jrxml").getInputStream();

            // compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // get datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);

            // add parameters
            Map<String, Object> parameters = new HashMap<>();


            // fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            System.out.println("Report generated successfully");

            // export
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to generate report");
        }
    }


}
