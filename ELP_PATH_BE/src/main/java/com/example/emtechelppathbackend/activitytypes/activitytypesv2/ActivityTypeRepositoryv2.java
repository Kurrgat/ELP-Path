package com.example.emtechelppathbackend.activitytypes.activitytypesv2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityTypeRepositoryv2 extends JpaRepository<ActivityTypeV2, Long> {
}
