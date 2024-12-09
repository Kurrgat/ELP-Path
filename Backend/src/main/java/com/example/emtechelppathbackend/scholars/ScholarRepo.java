package com.example.emtechelppathbackend.scholars;

import com.example.emtechelppathbackend.application.wtf_application.Gender;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ScholarRepo extends JpaRepository<Scholar, Long>, JpaSpecificationExecutor<Scholar> {
    Set<Scholar> findScholarsByBranchId(Long branchId);
    Set<Scholar> findScholarsByScholarCategory(ScholarCategories category);
    Set<Scholar> findScholarsByGender(Gender gender);
    Set<Scholar> findScholarsByInstitutionId(Long institutionId);
    Set<Scholar> findScholarsByScholarType(ScholarType type);



    @Query(value = """
            SELECT * FROM scholar WHERE scholar_code=:scholarCode
            """,nativeQuery = true)
    Optional<Scholar> findScholarByScholarCode(String scholarCode);
    Optional<Scholar> findScholarByPfNumber(String pfNumber);

    //Optional<Scholar> findScholarByUserId(Long userId);

    @Query("SELECT COUNT(s) FROM Scholar s")
    long getTotalScholars();

    @Query(value = """
           SELECT * FROM scholar WHERE pf_number=:pfNumberScholarCode OR scholar_code=:pfNumberScholarCode
            """,nativeQuery = true)
    Optional<Scholar> findScholarByScholarCodePfNumber(String pfNumberScholarCode);

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE gender='FEMALE'
        """, nativeQuery = true)
    Integer getTotalFemaleScholars();

    @Query(value = """
            SELECT year_of_joining_high_school_program, COUNT(*) as female_count
            FROM scholar WHERE gender = 'FEMALE' GROUP BY year_of_joining_high_school_program
            """, nativeQuery = true)
    List<Object[]> getTotalFemaleScholarsPerYear();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE gender='MALE'
        """, nativeQuery = true)
    Integer getTotalMaleScholars();

    @Query(value = """
            SELECT year_of_joining_high_school_program, COUNT(*) as male_count 
            FROM scholar WHERE gender = 'MALE' GROUP BY year_of_joining_high_school_program
            """, nativeQuery = true)
    List<Object[]> getTotalMaleScholarsPerYear();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE scholar_type='LOCAL'
        """, nativeQuery = true)
    Integer getTotalLocalScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE scholar_type='GLOBAL'
        """, nativeQuery = true)
    Integer getTotalGlobalScholars();

    //based on donors
   @Query(value = """ 
           SELECT COUNT(DISTINCT donor) FROM scholar
           """, nativeQuery = true)
    Integer getTotalDonors();
    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='MCF'
        """, nativeQuery = true)
    Integer getTotalMCFScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='DFID'
        """, nativeQuery = true)
    Integer getTotalDFIDScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='USAID'
        """, nativeQuery = true)
    Integer getTotalUSAIDScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='HSBC'
        """, nativeQuery = true)
    Integer getTotalHSBCScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='KFW'
        """, nativeQuery = true)
    Integer getTotalKFWScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='VITOL'
        """, nativeQuery = true)
    Integer getTotalVitolScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='CHA'
        """, nativeQuery = true)
    Integer getTotalCHAScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='RAO'
        """, nativeQuery = true)
    Integer getTotalRAOScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='DCL'
        """, nativeQuery = true)
    Integer getTotalDCLScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='MFI'
        """, nativeQuery = true)
    Integer getTotalMFIScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='GG'
        """, nativeQuery = true)
    Integer getTotalGGScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='SS'
        """, nativeQuery = true)
    Integer getTotalSSScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='EGF'
        """, nativeQuery = true)
    Integer getTotalEGFScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='FJ'
        """, nativeQuery = true)
    Integer getTotalFJScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='EBLE'
        """, nativeQuery = true)
    Integer getTotalEBLEScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='EGFI'
        """, nativeQuery = true)
    Integer getTotalEGFIScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='AS'
        """, nativeQuery = true)
    Integer getTotalASScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='TH009'
        """, nativeQuery = true)
    Integer getTotalTH009Scholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='MMF'
        """, nativeQuery = true)
    Integer getTotalMMFScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='UHUK'
        """, nativeQuery = true)
    Integer getTotalUHUKScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='FR'
        """, nativeQuery = true)
    Integer getTotalFRScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='DPM'
        """, nativeQuery = true)
    Integer getTotalDPMScholars();

    @Query(value = """
        SELECT COUNT(*) FROM scholar
        WHERE donor='EVT'
        """, nativeQuery = true)
    Integer getTotalEVTScholars();
    @Query(nativeQuery = true, value = "select case when TRIM(donor) = '' then 'OTHER' else donor end as donor, count(*) as count, year(year_of_joining_tertiary_program) as year from scholar where YEAR(year_of_joining_tertiary_program)=:year GROUP BY donor")
    List<DonorInterface> getDonorsPerYear(@Param("year") String year);

    @Query(nativeQuery = true, value = "select case when TRIM(donor) = '' then 'OTHER' else donor end as donor, count(*) as count from scholar group by donor order by count desc")
    List<DonorInterface> getDonors();
    @Query(nativeQuery = true, value = " SELECT `scholar`.*, `i`.`id` AS `institution_id` FROM `scholar` LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` WHERE `i`.`category` = 'GLOBAL_UNIVERSITY' AND YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year ")
    List<Scholar> getAllGlobalScholars(@Param("year") String year);


    @Query(nativeQuery = true, value = "SELECT `scholar`.*, `i`.`id` AS `institution_id` FROM `scholar` LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` WHERE `i`.`category` = 'LOCAL_UNIVERSITY' AND YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year")
    List<Scholar> getAllLocalScholars(@Param("year") String year);

    @Query(nativeQuery = true, value = "SELECT `scholar`.*, `i`.`id` AS `institution_id` FROM `scholar` LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` WHERE `i`.`category` = 'LOCAL_TVET' AND YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year")
    List<Scholar> getAllTvetScholars(@Param("year") String year);

    @Query(value = "SELECT YEAR(s.year_of_joining_high_school_program) as year, COUNT(*) as count FROM scholar s WHERE s.gender = 'MALE' GROUP BY s.year_of_joining_high_school_program", nativeQuery = true)
    List<GenderCountInterface> getMalesCount();

    @Query(value = "SELECT YEAR(s.year_of_joining_high_school_program) as year, COUNT(*) as count FROM scholar s WHERE s.gender = 'FEMALE' GROUP BY s.year_of_joining_high_school_program", nativeQuery = true)
    List<GenderCountInterface> getFemalesCount();


    @Query(nativeQuery = true, value =
            "SELECT \n" +
                    "    'All Time' AS year,\n" +
                    "    COUNT(*) AS scholarCount,\n" +
                    "    SUM(CASE WHEN s.gender = 'MALE' THEN 1 ELSE 0 END) AS maleCount,\n" +
                    "    SUM(CASE WHEN s.gender = 'FEMALE' THEN 1 ELSE 0 END) AS femaleCount,\n" +
                    "    COUNT(DISTINCT s.donor) AS donors,\n" +
                    "    COUNT(DISTINCT s.scholar_high_school) AS highSchool,\n" +
                    "    SUM(CASE WHEN i.category = 'LOCAL_UNIVERSITY' THEN 1 ELSE 0 END) AS localScholars,\n" +
                    "    SUM(CASE WHEN i.category = 'REGIONAL_UNIVERSITY' THEN 1 ELSE 0 END) AS regionalScholars,\n" +
                    "    SUM(CASE WHEN i.category = 'GLOBAL_UNIVERSITY' THEN 1 ELSE 0 END) AS globalScholars,\n" +
                    "    SUM(CASE WHEN i.category = 'LOCAL_TVET' THEN 1 ELSE 0 END) AS tvetScholars,\n" +
                    "    COUNT(DISTINCT CASE WHEN i.category = 'LOCAL_UNIVERSITY' THEN i.id END) AS localVersity,\n" +
                    "    COUNT(DISTINCT CASE WHEN i.category = 'REGIONAL_UNIVERSITY' THEN i.id END) AS regionalVarsity,\n" +
                    "    COUNT(DISTINCT CASE WHEN i.category = 'GLOBAL_UNIVERSITY' THEN i.id END) AS globalVarsity,\n" +
                    "    COUNT(DISTINCT CASE WHEN i.category = 'LOCAL_TVET' THEN i.id END) AS tvets\n" +
                    "FROM \n" +
                    "    scholar s\n" +
                    "LEFT JOIN \n" +
                    "    institution i ON s.scholar_tertiary_institution = i.id\n" +
                    "WHERE \n" +
                    "    (:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year)\n"
    )


    ScholarStatsInterface getScholarStats(@Param("year") String year, Boolean ignoreCondition );

    @Query(value = """
            SELECT sc.scholarFirstName,sc.id,sc.scholarCode,sc.pfNumber,sc.scholarLastName,sc.donor,sc.gender,sc.branch,sc.name,sc.scholarCategory,sc.scholarType,sc.yearOfJoiningHighSchoolProgram,
            sc.school,sc.institution FROM (SELECT s.id,s.scholar_code AS scholarCode,s.pf_number AS pfNumber, s.scholar_first_name AS scholarFirstName, s.scholar_last_name AS scholarLastName,s.donor,
            s.gender,b.branch_name AS branch,c.name,s.scholar_category AS scholarCategory,s.scholar_type AS scholarType,s.year_of_joining_high_school_program AS yearOfJoiningHighSchoolProgram,
            sc.school_name AS school,i.name AS institution
            FROM  scholar AS s JOIN branch_details AS b ON s.scholar_branch=b.id JOIN kenyan_county AS c ON s.home_county_id=c.id
            JOIN school_details AS sc ON s.scholar_high_school=sc.id JOIN institution AS i ON s.scholar_tertiary_institution=i.id) AS sc
            WHERE (:gender IS NULL OR sc.gender = :gender) AND (:donor IS NULL OR sc.donor = :donor)
            AND (:branch IS NULL OR sc.branch = :branch) AND (:institution IS NULL OR sc.institution = :institution)
            AND (:scholarCategory IS NULL OR sc.scholarCategory = :scholarCategory) AND (:scholarType IS NULL OR sc.scholarType = :scholarType)          
            """, nativeQuery = true)
    Set<ScholarDTO_2> filterScholars(String gender, String donor, String branch, String institution, String scholarCategory, String scholarType);

    @Query(nativeQuery = true, value = """
    SELECT s.*, i.id AS institution_id, i.name AS institution_name, i.category AS institution_category
    FROM scholar s
    LEFT JOIN institution i ON s.scholar_tertiary_institution = i.id
    WHERE YEAR(s.year_of_joining_tertiary_program) = :year
    """)
    List<Scholar> getAllScholarsPerYear(@Param("year") String year);


    @Query(nativeQuery = true, value ="SELECT `scholar`.*, `i`.`id` AS `institution_id` FROM `scholar` LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` WHERE `i`.`category` ='REGIONAL_UNIVERSITY' AND YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year")
    List<Scholar> getRegionalScholars(@Param("year")String year);

    @Query(nativeQuery = true, value =" select concat(s.scholar_first_name,' ',s.scholar_last_name) as Name, s.scholar_code as Code, s.gender as Gender, b.branch_name as Branch from scholar s left join branch_details b on s.scholar_branch = b.id WHERE YEAR(s.year_of_joining_tertiary_program) = :year ")
    List<ScholarReportInterface> getScholarsPerYear(@Param("year") String year);


//    List<ScholarReportInterface> getLocalScholarsPerYear(String year);
}
