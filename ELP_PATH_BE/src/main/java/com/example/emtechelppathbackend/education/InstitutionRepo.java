package com.example.emtechelppathbackend.education;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepo extends JpaRepository<Institution, Long> {

    @Query(value = "SELECT * FROM institution WHERE id = :institutionId",nativeQuery = true)
    Optional<Institution> findByDataId(Long institutionId);

//    interface InstitutionChapter{
//        Long getChapterId();
//        Long getRegionId();
//        String getCategory();
//        Long getInstitutionId();
//    }

    interface InstitutionInterface{
    Long getId();
    String getCountryName();
    String getCode();
    String getInstitutionName();

}
    @Query(value = "SELECT i.id AS id, c.name AS countryName, i.country_code AS code, i.name AS institutionName \n" +
            "            FROM country c LEFT JOIN institution i ON c.country_code = i.country_code \n" +
            "            WHERE c.name = :countryName", nativeQuery = true)
    List<InstitutionInterface> findByCountryName(@Param("countryName") String countryName);




}
