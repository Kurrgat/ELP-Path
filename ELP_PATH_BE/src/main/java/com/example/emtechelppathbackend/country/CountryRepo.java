package com.example.emtechelppathbackend.country;

import com.example.emtechelppathbackend.education.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepo extends JpaRepository<Country,Long> {
@Query(value = "SELECT c.id AS id,c.name AS countryName,c.country_code AS code, c.dial_code AS dialCode FROM country c",nativeQuery = true)
    List<CountryInterface> findAllCountryCodes();

interface CountryInterface{
    Long getId();
    String getDialCode();
    String getCountryName();
    String getCode();


}
}
