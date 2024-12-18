package com.example.emtechelppathbackend.otp;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {


    List<OtpEntity> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
