package com.example.emtechelppathbackend.auditing;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditingRepo extends JpaRepository<Auditing, Long> {

}
