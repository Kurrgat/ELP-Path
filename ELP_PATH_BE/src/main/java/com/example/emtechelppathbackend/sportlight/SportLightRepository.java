package com.example.emtechelppathbackend.sportlight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportLightRepository extends JpaRepository<SportLight, Long> {
}
