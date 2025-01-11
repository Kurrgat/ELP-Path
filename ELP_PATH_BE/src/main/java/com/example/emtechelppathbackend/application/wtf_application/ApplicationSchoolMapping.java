/*
package com.example.emtechelppathbackend.branchchampion.entities;

import com.example.emtechelppathbackend.branchchampion.entities.scholars.School;
import com.fasterxml.jackson.annotation.JsonBackReference;
=======
package com.example.emtechelppathbackend.Application;

import com.example.emtechelppathbackend.Application.Application;
import com.example.emtechelppathbackend.School.School;
>>>>>>> 138fd67a7bcdf4a7f12016b2190f8f8f6dd5d346:src/main/java/com/example/emtechelppathbackend/Application/ApplicationSchoolMapping.java
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_school_mapping")
public class ApplicationSchoolMapping {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @JsonFormat(pattern = "yyyy-MM-dd")
	  private LocalDate startDate; // Start date when the applicant joined the school

	  @JsonFormat(pattern = "yyyy-MM-dd")
	  private LocalDate endDate; // End date when the applicant left the school


	  @ManyToOne
	  @JoinColumn(name = "application_id")
	  @JsonBackReference//breaking the circular reference
	  private Application application;

	  @ManyToOne
	  @JoinColumn(name = "school_id")
	  @JsonBackReference//breaking the circular reference
	  private School school;

	  public ApplicationSchoolMapping(Application application, School school) {
		    this.application = application;
		    this.school = school;
	  }

	  //solving the stack overflow issue due to circular reference
	  @Override
	  public int hashCode() {
		    return Objects.hash(id);
	  }
}
 */
