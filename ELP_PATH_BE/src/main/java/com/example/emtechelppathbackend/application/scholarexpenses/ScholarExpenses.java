package com.example.emtechelppathbackend.application.scholarexpenses;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.school.School;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "expenses")
public class ScholarExpenses {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  //   Why not use the school field in entity Application to fetch the school name?
//    private String schoolName;
	  private Long form;
	  private Long term;
	  @Column(nullable = false)
	  @JsonFormat(pattern = "yyyy-MM-dd")
	  private LocalDate start_date;
	  private Integer schoolFees;
	  private Integer shopping;
	  private Integer transport;
	  private Integer upkeep;

	  private Integer scholarTotalPerTerm;
	  /*  @ManyToOne(fetch = FetchType.LAZY)
	    @JsonIgnore
	    @JoinColumn(name = "scholar_id")
	    private Scholar scholar;*/
	  @NotNull(message = "Each expense entry should be associated to a scholar")
	  @ManyToOne(fetch = FetchType.LAZY)
	  @JsonIgnore
	  @JoinColumn(name = "application_id")
	  private Application application;

	  @ManyToOne(fetch = FetchType.LAZY)
	  @JsonManagedReference
	  @JoinColumn(name = "school_id")
	  private School school;

	  public Integer getScholarTotalPerTerm() {

		    return schoolFees + shopping + transport + upkeep;
	  }

	  public void setScholarTotalPerTerm(Integer scholarTotalPerTerm) {
		    this.scholarTotalPerTerm = scholarTotalPerTerm;
	  }
}
