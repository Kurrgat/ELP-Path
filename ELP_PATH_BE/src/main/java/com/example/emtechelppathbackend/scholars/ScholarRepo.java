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

import java.util.*;

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
    @Query(nativeQuery = true, value = " SELECT `scholar`.*, `i`.`id` AS `institution_id` \n" +
            "FROM `scholar`\n" +
            " LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` \n" +
            " WHERE `i`.`category` = 'GLOBAL_UNIVERSITY' \n" +
            "AND (:ignoreCondition = true OR YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year)\n")
    List<Scholar> getAllGlobalScholars( String year,Boolean ignoreCondition );


    @Query(nativeQuery = true, value = "SELECT `scholar`.*, `i`.`id` AS `institution_id` \n" +
            "FROM `scholar` \n" +
            "LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` \n" +
            "WHERE `i`.`category` = 'LOCAL_UNIVERSITY' \n" +
            "AND (:ignoreCondition = true OR YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year)\n")
    List<Scholar> getAllLocalScholars(String year,  Boolean ignoreCondition);

    @Query(nativeQuery = true, value = "SELECT `scholar`.*, `i`.`id` AS `institution_id` FROM `scholar` \n" +
            "LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` \n" +
            "WHERE `i`.`category` = 'LOCAL_TVET' \n" +
            "AND (:ignoreCondition = true OR YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year)\n")
    List<Scholar> getAllTvetScholars(String year, Boolean ignoreCondition);

    @Query(value = "SELECT YEAR(s.year_of_joining_high_school_program) as year, COUNT(*) as count FROM scholar s WHERE s.gender = 'MALE' GROUP BY s.year_of_joining_high_school_program", nativeQuery = true)
    List<GenderCountInterface> getMalesCount();

    @Query(value = "SELECT YEAR(s.year_of_joining_high_school_program) as year, COUNT(*) as count FROM scholar s WHERE s.gender = 'FEMALE' GROUP BY s.year_of_joining_high_school_program", nativeQuery = true)
    List<GenderCountInterface> getFemalesCount();


    @Query(nativeQuery = true, value =
            "SELECT \n" +
                    "    'All Time' AS YEAR,\n" +
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
                    "    COUNT(DISTINCT CASE WHEN i.category = 'LOCAL_TVET' THEN i.id END) AS tvets,\n" +
                    "    SUM(case when e.education_type='Alumni' then 1 ELSE 0 END) AS alumni,\n" +
                    "    SUM(case when e.education_type='CurrentScholar' then 1 ELSE 0 END) AS currentscholar\n" +
                    "FROM \n" +
                    "    scholar s\n" +
                    "LEFT JOIN \n" +
                    "    institution i ON s.scholar_tertiary_institution = i.id\n" +
                    "Left JOIN \n" +
                    "user_details u ON s.id=u.scholar_id\n" +
                    "LEFT JOIN education e ON u.id=e.users_id\n" +
                    "\n" +
                    "WHERE \n" +
                    "    (:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year);\n"
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
                        WHERE (:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year)
            """)
    List<Scholar> getAllScholarsPerYear( String year, Boolean ignoreCondition);


    @Query(nativeQuery = true, value ="SELECT `scholar`.*, `i`.`id` AS `institution_id` FROM `scholar` LEFT JOIN `institution` i ON `scholar`.`scholar_tertiary_institution` = `i`.`id` \n" +
            "WHERE `i`.`category` ='REGIONAL_UNIVERSITY' \n" +
            "AND (:ignoreCondition = true OR YEAR(`scholar`.`year_of_joining_tertiary_program`) =:year)\n")
    List<Scholar> getRegionalScholars(String year, Boolean ignoreCondition);

//    @Query(nativeQuery = true, value = "select concat(s.scholar_first_name,' ',s.scholar_last_name) as Name,\n" +
//            "  s.scholar_code as Code, s.gender as Gender, b.branch_name as Branch from scholar s \n" +
//            "  left join branch_details b on s.scholar_branch = b.id\n" +
//            "  WHERE (:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year)")
//    List<ScholarReportInterface> getScholarsPerYear(String year, Boolean ignoreCondition);
    @Query(nativeQuery = true, value = "SELECT  s.*\n" +
            "FROM scholar s\n" +
            "LEFT JOIN \n" +
            "    institution i ON s.scholar_tertiary_institution = i.id\n" +
            "Left JOIN \n" +
            "user_details u ON s.id=u.scholar_id\n" +
            "LEFT JOIN education e ON u.id=e.users_id  \n" +
            "WHERE e.education_type=\"Alumni\"\n" +
            "AND (:ignoreCondition = true OR YEAR(year_of_joining_tertiary_program) = :year)\n ")
    Scholar getAlumni(String year, Boolean ignoreCondition);

    @Query(nativeQuery = true, value = "SELECT s.*\n" +
            "FROM scholar s\n" +
            "LEFT JOIN \n" +
            "    institution i ON s.scholar_tertiary_institution = i.id\n" +
            "Left JOIN \n" +
            "user_details u ON s.id=u.scholar_id\n" +
            "LEFT JOIN education e ON u.id=e.users_id  \n" +
            "WHERE e.education_type=\"CurrentScholar\"\n" +
            "AND (:ignoreCondition = true OR YEAR(year_of_joining_tertiary_program) = :year)\n ")
    Scholar getCurrentScholars(String year, Boolean ignoreCondition);
    interface ScholarReportInterface {
            Integer getScholarId();
        String getName();
        String getCode();
        String getGender();
        String getBranch();
        String getScholarType();
    }

    @Query(nativeQuery = true, value = "SELECT s.id AS scholarId, CONCAT(s.scholar_first_name, ' ', s.scholar_last_name) AS name,\n" +
            "            s.scholar_code AS code,s.scholar_type AS scholarType, s.gender AS gender, b.branch_name AS branch \n" +
            "            FROM scholar s \n" +
            "            LEFT JOIN branch_details b ON s.scholar_branch = b.id \n" +
            "            WHERE ((:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year)\n" +
            "            AND (:ignoreCondition = true OR ( s.gender = :gender)) \n" +
            "            AND (:ignoreCondition = true OR ( b.branch_name = :branch)) \n" +
            "            AND (:ignoreCondition = true OR (s.scholar_type = :scholarType)))")
    Collection<ScholarReportInterface> getScholars(String year, String gender, String branch, String scholarType, Boolean ignoreCondition);

    interface RegisteredAndUnregisteredCount{
    Long getRegisteredUsers();
    Long getUnregisteredUsers();


}
    @Query(nativeQuery = true,value = "SELECT\n" +
        "  COUNT(DISTINCT ud.id) AS RegisteredUsers,\n" +
        "  (SELECT COUNT(*) FROM scholar s WHERE NOT EXISTS (SELECT 1 FROM user_details ud WHERE s.id = ud.scholar_id)) AS UnregisteredUsers\n" +
        "FROM\n" +
        "  user_details ud\n" +
        "JOIN\n" +
        "  scholar s ON ud.scholar_id = s.id\n" +
        "WHERE\n" +
        "  ud.id != 1")
    RegisteredAndUnregisteredCount getUserCount();

    interface MaleScholarsInterface{
    Long getId();
    String getGender();
    String getFirstName();
    String getLastName();
    String getBranch();

}
    @Query(nativeQuery = true,value = "SELECT \n" +
            "    s.id AS Id, \n" +
            "    s.gender AS gender, \n" +
            "    s.scholar_first_name AS firstName, \n" +
            "    s.scholar_last_name AS lastName, \n" +
            "    b.branch_name AS branch\n" +
            "FROM \n" +
            "    scholar s\n" +
            "LEFT JOIN \n" +
            "    institution i ON s.scholar_tertiary_institution = i.id\n" +
            "LEFT JOIN \n" +
            "    branch_details b ON s.scholar_branch = b.id\n" +
            "WHERE \n" +
            "    s.gender = 'MALE'\n" +
            "AND \n" +
            "    (:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year);\n")
    List<MaleScholarsInterface> getMaleScholars(String year, Boolean ignoreCondition);
interface FemaleScholarsInterface{
    Long getId();
    String getGender();
    String getFirstName();
    String getLastName();
    String getBranch();
}
    @Query(nativeQuery = true,value = "SELECT \n" +
            "    s.id AS Id, \n" +
            "    s.gender AS gender, \n" +
            "    s.scholar_first_name AS firstName, \n" +
            "    s.scholar_last_name AS lastName, \n" +
            "    b.branch_name AS branch\n" +
            "FROM \n" +
            "    scholar s\n" +
            "LEFT JOIN \n" +
            "    institution i ON s.scholar_tertiary_institution = i.id\n" +
            "LEFT JOIN \n" +
            "    branch_details b ON s.scholar_branch = b.id\n" +
            "WHERE \n" +
            "    s.gender = 'FEMALE'\n" +
            "AND \n" +
            "    (:ignoreCondition = true OR YEAR(s.year_of_joining_tertiary_program) = :year);\n" )
    List<FemaleScholarsInterface> getFemaleScholars(String year, Boolean ignoreCondition);



}
