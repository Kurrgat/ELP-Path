package com.example.emtechelppathbackend.reports;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public interface ReportService {
    byte[] generateScholarReport(String year,String gender,String branch,String scholarType,Boolean ignoreCondition);

    byte[] generateReport();

    byte[] generateEventsReport(LocalDate startDate, LocalDate endDate);

    byte[] generateChapterReport();

    byte[] generateJobOpportunityReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate);

    byte[] generateHubsReport();
}
