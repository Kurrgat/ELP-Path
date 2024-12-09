package com.example.emtechelppathbackend.security.user;


import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.security.roles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import java.util.Collection;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "user_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = "userEmail")
})
public class Users  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime RegisteringDate;

    @Column(nullable = false)
    private String userEmail;

    private String userPassword;

    private String username;

    private String firstName;

    private String lastName;

    private String passwordResetToken;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime lastModified;

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long modifiedBy;

    @Column(name = "acceptedTermsAndConditions", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean acceptedTermsAndConditions;

    @ManyToOne
    @JoinColumn(name = "user_roles")
    private Role role;

    @OneToOne
     private Scholar scholar;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                 .toList();
    }

    @Override
     public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return  userEmail;
    }

}