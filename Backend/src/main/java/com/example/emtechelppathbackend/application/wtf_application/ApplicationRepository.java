package com.example.emtechelppathbackend.application.wtf_application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	  Set<Application> findApplicationsByBranchId(Long branchId);

	  Optional<Application> findApplicationByScholarCodeAndUserIsNotNull(String scholarCode);

	  Optional<Application> findApplicationByScholarCode(String scholarCode);
}
