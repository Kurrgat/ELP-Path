package com.example.emtechelppathbackend.education;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
