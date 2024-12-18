package com.example.emtechelppathbackend.skills;

import com.example.emtechelppathbackend.security.user.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {

    @Query(value = "SELECT * FROM user_skills WHERE user_id = :userId AND id= :skillId", nativeQuery = true)
    Optional<Skills> findByUsersAndId(Long userId, Long skillId);




    @Query(value = "SELECT COUNT(*) AS count\n" +
            "FROM user_skills\n" +
            "WHERE user_id = :userId AND technical_level IS NOT NULL", nativeQuery = true)
    long countTechnicalSkillsByUserId(Long userId);
    @Query(value = "SELECT COUNT(*) AS count\n" +
            "FROM user_skills\n" +
            "WHERE user_id = :userId AND language_level IS NOT NULL", nativeQuery = true)
    long countLanguageSkillsByUserId(Long userId);
    @Query(value = "SELECT COUNT(*) AS count\n" +
            "FROM user_skills\n" +
            "WHERE user_id = :userId AND soft_skills_level IS NOT NULL", nativeQuery = true)
    long countSoftSkillsByUserId(Long userId);

    Skills findBySkillsNameAndUsers(String skillsName, Users user);

    interface SoftSkillInterface{
        Long getId();
        String getSkillName();
        String getSoftSkillLevel();
    }
    @Query(value = "SELECT us.id AS Id, us.skills_name AS skillName, us.soft_skills_level AS softSkillLevel\n" +
            "FROM user_skills us \n" +
            "WHERE user_id = :userId AND us.soft_skills_level IS NOT NULL ", nativeQuery = true)
    List<SoftSkillInterface> findSoftSkillLevelByUserId(Long userId);

    interface LanguageInterface{
        Long getId();
        String getSkillName();
        String getLanguageLevel();
    }
    @Query(value = "SELECT us.id AS Id, us.skills_name AS skillname, us.language_level AS languageLevel\n" +
            "FROM user_skills us \n" +
            "WHERE user_id = :userId AND us.language_level IS NOT NULL ", nativeQuery = true)
    List <LanguageInterface>findLanguageSkillsByUserId(Long userId);

    interface TechnicalInterface{
        Long getId();
        String getSkillName();
        String getTechnicalLevel();


    }

    @Query(value = "SELECT us.id AS Id, us.skills_name AS skillName, us.technical_level AS technicalLevel\n" +
            "FROM user_skills us \n" +
            "WHERE user_id = :userId AND us.technical_level IS NOT NULL", nativeQuery = true)
    List<TechnicalInterface> findTechnicalLevelByUserId( Long userId);

    @Query(value = "SELECT * FROM user_skills WHERE user_id = :userId", nativeQuery = true)
    List<Skills> findByUserId(Long userId);
}