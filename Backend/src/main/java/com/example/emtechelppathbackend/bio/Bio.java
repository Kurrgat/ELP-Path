package com.example.emtechelppathbackend.bio;

import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bio")
public class Bio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(max = 5000)
    private String description;
    @OneToOne
    @JoinColumn(name = "users_id")
    private Users user;
}
