package com.example.emtechelppathbackend.branch;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branch_details")
public class Branch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime recordDate = LocalDateTime.now();

	@NotNull(message = "A branch should have a name")
	private String branchName;

	@NotNull(message = "A branch code should be provided")
	@Column(unique = true)
	private String branchCode;
	private String bankCode;
	private String additionalInformation;

	//solving the stack overflow issue due to circular reference
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
