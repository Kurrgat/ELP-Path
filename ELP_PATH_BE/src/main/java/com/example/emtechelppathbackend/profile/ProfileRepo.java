package com.example.emtechelppathbackend.profile;


import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProfileRepo extends JpaRepository<Profile,Long> {
  //  @Query("SELECT user FROM profile WHERE profile.user_id=:userId")
  Profile findProfileByUserId(Long userId);

  @Query(value = "SELECT profile_image FROM profile WHERE users_id=:userId", nativeQuery = true)
  Optional<String> findProfileImageByUserId(Long userId);

  @Query(value = "SELECT CONCAT(u.first_name, ' ', u.last_name) AS scholarName, " +
          "b.branch_name AS branchName, sh.school_name AS school, " +
          "s.id AS scholarId, p.profile_image AS profileImage, " +
          "u.id AS userId, p.id AS profileId, i.name AS institution " +
          "FROM scholar s " +
          "JOIN institution i ON s.scholar_tertiary_institution = i.id " +
          "JOIN school_details sh ON s.scholar_high_school = sh.id " +
          "JOIN branch_details b ON s.scholar_branch = b.id " +
          "LEFT JOIN user_details u ON s.id = u.scholar_id " +
          "LEFT JOIN profile p ON u.id = p.users_id " +
          "WHERE sh.school_name = :schoolName AND b.branch_name = :branchName " +
          "AND u.id IS NOT NULL " +
          "ORDER BY p.id DESC, s.id ASC", nativeQuery = true)
  List<MorePeopleInterface> getPeopleIKnow(String schoolName, String branchName);

  @Query(value = "SELECT CONCAT(u.first_name, ' ', u.last_name) AS scholarName, " +
          "b.branch_name AS branchName, sh.school_name AS school, " +
          "s.id AS scholarId, p.profile_image AS profileImage, " +
          "u.id AS userId, p.id AS profileId, i.name AS institution " +
          "FROM scholar s " +
          "JOIN institution i ON s.scholar_tertiary_institution = i.id " +
          "JOIN school_details sh ON s.scholar_high_school = sh.id " +
          "JOIN branch_details b ON s.scholar_branch = b.id " +
          "LEFT JOIN user_details u ON s.id = u.scholar_id " +
          "LEFT JOIN profile p ON u.id = p.users_id " +
          "WHERE u.id IS NOT NULL " +
          "ORDER BY u.id DESC, s.id ASC " +
          "LIMIT 10", nativeQuery = true)
  List<MorePeopleInterface> getAvailablePeople();

    Profile findByUser(Users user);
@Query(value = "SELECT * FROM profile WHERE users_id = :userId", nativeQuery = true)
    Optional<Profile> findByUserId(Long userId);

}