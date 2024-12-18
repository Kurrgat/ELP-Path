package com.example.emtechelppathbackend.learning.enrol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolRepository extends JpaRepository<Enrol, Long> {
}
