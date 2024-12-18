package com.example.emtechelppathbackend.counties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountyRepo extends JpaRepository<KenyanCounty,Long> {
}
