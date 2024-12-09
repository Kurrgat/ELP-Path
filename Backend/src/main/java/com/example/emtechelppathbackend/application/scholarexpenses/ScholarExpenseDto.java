package com.example.emtechelppathbackend.application.scholarexpenses;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.school.School;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScholarExpenseDto {
	private Long id;
	private Long form;
	private Long term;
	private School school;
	private LocalDate start_date;
	private Integer schoolFees;
	private Integer shopping;
	private Integer transport;
	private Integer upkeep;
	private Integer total;
	@JsonIgnore//Properties("hibernateLazyInitializer")
	private Application application;
}
