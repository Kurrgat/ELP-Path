package com.example.emtechelppathbackend.scholars;


import java.time.LocalDate;

public record VerifyScholarRequest(String pfNumberOrscholarCode, String firstName, String lastName, String middleName,Long highSchoolId, Long homeBranchId) {
}
