package com.example.emtechelppathbackend.education;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepo extends JpaRepository<Institution, Long> {
}
