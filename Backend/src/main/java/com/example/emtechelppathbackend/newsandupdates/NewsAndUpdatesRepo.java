package com.example.emtechelppathbackend.newsandupdates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsAndUpdatesRepo extends JpaRepository<NewsAndUpdates, Long> {

}
