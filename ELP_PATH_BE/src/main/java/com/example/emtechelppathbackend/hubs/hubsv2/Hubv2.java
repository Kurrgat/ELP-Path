package com.example.emtechelppathbackend.hubs.hubsv2;

import com.example.emtechelppathbackend.scholars.Scholar;
import com.example.emtechelppathbackend.security.user.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "Hubsv2")
public class Hubv2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hubName;
    @Length(max = 10000)
    private String hubDescription;

    private String hubImage;

    @ManyToOne(cascade = CascadeType.ALL)
    private Users hubAdmin;

}
