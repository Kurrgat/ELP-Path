package com.example.emtechelppathbackend.institutionchecker;

import com.example.emtechelppathbackend.utils.CustomResponse;

public interface InstitutionCheckerService {

    CustomResponse<?> addInstitutionChecker(InstitutionChecker institutionChecker);
}
