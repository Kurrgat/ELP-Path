package com.example.emtechelppathbackend.application.schoolhistory;

import com.example.emtechelppathbackend.application.wtf_application.Application;
import com.example.emtechelppathbackend.school.School;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "school_history")
public class SchoolHistory {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne
	  @JoinColumn(name = "application_id")
	  private Application application;

	  /*@ManyToOne
	  @JoinColumn(name = "scholar_id")
	  private Elimu elimuScholar;*/

	  @ManyToOne
	  @JoinColumn(name = "school_id")
	  private School school;

	  private LocalDate startDate;

	  private LocalDate transferDate;

	  private LocalDate endDate;

	  public SchoolHistory(Application application, School school) {
		    this.application = application;
		    this.school = school;
		    this.transferDate = LocalDate.now();
	  }

	  /*
	public SchoolHistory(Elimu scholar, School school) {
		this.elimuScholar = scholar;
		this.school = school;
		this.transferDate = LocalDate.now();
	}*/

	  @Override
	  public int hashCode() {
		    return Objects.hash(id);
	  }
}
