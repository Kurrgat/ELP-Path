package com.example.emtechelppathbackend.education;

import com.example.emtechelppathbackend.security.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface EducationRepo extends JpaRepository<Education,Long> {
//    @Query("SELECT education FROM Education WHERE education.profile_id=:profileId")
    
    List<Education> findByUserId(Long userId);

    @Query(value = "SELECT cc.name AS CourseCluster, c.id, c.name, c.course_cluster_id AS CourseClusterId FROM course c JOIN course_cluster cc ON c.course_cluster_id=cc.id;",nativeQuery = true)
    List<CourseInterface> fetchCourses();

    @Query(value = "SELECT c.id AS Id, c.name AS Name, c.course_cluster_id AS CourseClusterId, cc.name AS CourseCluster FROM course c JOIN course_cluster cc ON c.course_cluster_id=cc.id WHERE cc.id=:clusterID",nativeQuery = true)
    List<CourseInterface> fetchCoursesByCluster(Long clusterID);
    @Query(value = "SELECT id,name FROM course_cluster",nativeQuery = true)
    List<CourseClusterInterface> fetchCourseClusters();

    @Query(value = "SELECT id,name FROM course_cluster WHERE id=:clusterID",nativeQuery = true)
    Optional<CourseClusterInterface> findCourseClusterById(Long clusterID);

    @Query(value = "SELECT c.id, c.name, c.course_cluster_id AS CourseClusterId, cc.name AS CourseCluster FROM course c JOIN course_cluster cc ON c.course_cluster_id=cc.id WHERE c.id=:courseId", nativeQuery = true)
    Optional<CourseInterface> findCourseById(Long courseId);


    @Query(value = "SELECT * FROM education WHERE users_id = :userId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Education> findUserEduStatus(@Param("userId") Long userId);

    List<Education> findByUser(Users user);
    @Query(nativeQuery = true, value = "SELECT course_level as courseLevel, COUNT(*) as count FROM education GROUP BY course_level")
    List<CourseLevelInterface> findCountByCourseLevel();
    interface EducationLevel{
        Long getId();
        String getCourseLevel();
        LocalDate getGraduationYear();
        String getName();

    }
    @Query(nativeQuery = true, value = " SELECT e.id AS id, e.course_level AS courseLevel, e.graduation_year AS graduationYear,\n" +
            " CONCAT(u.first_name, ' ', u.last_name) AS name\n" +
            "\n" +
            " FROM education e\n" +
            " LEFT JOIN  user_details u ON e.users_id = u.id \n" +
            "WHERE e.course_level = :educationLevel")
    List<EducationLevel> findByCourseLevel(String educationLevel);

    interface CourseLevelInterface{
        String getCourseLevel();
        Long getCount();
    }

    interface CourseClusterCount{
        Long getCount();
        String getName();
    }

    @Query(value = "SELECT COUNT(*) AS count, cl.name AS name FROM education e LEFT JOIN course c ON e.course_id = c.id LEFT JOIN course_cluster cl ON c.course_cluster_id = cl.id GROUP BY cl.name", nativeQuery = true)
    List<CourseClusterCount> countCourseClusters();



}
