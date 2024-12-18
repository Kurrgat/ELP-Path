package com.example.emtechelppathbackend.security.user;

import com.example.emtechelppathbackend.scholars.UserCountInterface;
import com.twilio.rest.bulkexports.v1.export.ExportCustomJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
interface UserInterface{
	 Long getId();

	 String getEmail();

	 String getFirstName();
}

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	  Optional<Users> findUsersByUserEmail(String userEmail);
	  @Query(value = "SELECT id,user_email AS email, first_name AS firstName FROM user_details",nativeQuery = true)
	  Optional<UsersRepository> test(Long id);
	  boolean existsByUserEmail(String userEmail);

	  Optional<Users> findUsersByPasswordResetToken(String passwordResetToken);

	  @Query("SELECT u FROM Users u WHERE u.role.roleName <> 'ALUMNI'")
	  List<Users> findUsersByRoleIsNotAlumni();

	  List<Users> findUsersByRole_RoleName(String roleName);

	  Long countUsersByRole_RoleName(String roleName);


	  Optional<Users> findUserByScholarId(Long id);


    Optional<Users> findUsersByUsername(String username);
	@Query(value = "SELECT sum(case when r.role_name='ALUMNI' then 1 else 0 end) AS alumniCount, sum(case when r.role_name='Chapter Admin' then 1 else 0 end) AS chapterAdminCount,sum(case when r.role_name=' Hub Admin' then 1 else 0 end) AS hubAdminCount,sum(case when r.role_name=' admin' then 1 else 0 end) AS admin, sum(case when r.role_name=\"SUPER_ADMIN\" then 1 else 0 end) as superAdminCount FROM user_details u left JOIN roles r ON u.user_roles = r.id;",nativeQuery = true)
	UserCountInterface countUsers();
	@Query(value = "SELECT * FROM user_details WHERE id = :userId",nativeQuery = true)
    Optional<Users> getUserById(Long userId);

    Users getUsersByScholarId(Long scholarId);
}
