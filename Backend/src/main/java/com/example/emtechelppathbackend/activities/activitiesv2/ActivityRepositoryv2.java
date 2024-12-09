package com.example.emtechelppathbackend.activities.activitiesv2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepositoryv2 extends JpaRepository<ActivityV2, Long> {

	 List<ActivityV2> findActivityByChapterId(Long chapterId);
	 List<ActivityV2> findActivityByHubId(Long hubId);

	  @Override
	/*@Query(value = """
			SELECT * FROM activity_details order by activity_date desc limit 10
			""", nativeQuery = true)*/
	List<ActivityV2> findAll();
}
