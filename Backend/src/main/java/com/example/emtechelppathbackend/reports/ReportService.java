package com.example.emtechelppathbackend.reports;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.scholars.ScholarRepo;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ScholarRepo scholarRepo;

    public void exportReport(String reportFormat){

        List <Scholar> scholars = scholarRepo.findAll();
    }
}
