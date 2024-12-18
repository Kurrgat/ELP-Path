package com.example.emtechelppathbackend.application.scholarexpenses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface ScholarExpenseRepo extends JpaRepository<ScholarExpenses,Long> {
    List<ScholarExpenses>findByApplicationId(Long applicationId);
}
