package com.example.emtechelppathbackend.application.scholarexpenses;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ScholarExpenseService {

    // Map<Integer, Integer> getTotalExpenseByYear();

    ScholarExpenseDto addScholarExpenses(Long scholarId, ScholarExpenseDto scholarExpenseDto);
    List<ScholarExpenseDto> findExpensesByApplicationId(Long scholarId);

    ScholarExpenseDto updateScholarExpenses(Long scholarExpensesId, ScholarExpenseDto updatedScholarEducationDto);

    void deleteScholarExpenseById(Long scholarExpenseId);

    int getTotalExpenditurePerScholar(Long applicationId);

    int getGrandExpenditureForAllScholars();

    Map<Integer, Integer> getTotalExpenseByYear();

}
