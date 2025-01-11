package com.example.emtechelppathbackend.activities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

	 List<Activity> findActivityByChapterId(Long chapterId);
	 List<Activity> findActivityByHubId(Long hubId);

	  @Override
	/*@Query(value = """
			SELECT * FROM activity_details order by activity_date desc limit 10
			""", nativeQuery = true)*/
	List<Activity> findAll();

}
