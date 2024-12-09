package com.example.emtechelppathbackend.privacy;


import com.example.emtechelppathbackend.branch.Branch;
import com.example.emtechelppathbackend.profile.JobStatus;
import com.example.emtechelppathbackend.profile.Profile;
import com.example.emtechelppathbackend.scholars.ScholarCategories;
import com.example.emtechelppathbackend.security.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivacyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean emailPrivate;

    private Boolean phoneNumberPrivate;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;


   // public void setIsEmailPrivate(boolean emailPrivate) {

        public void setIsEmailPrivate (Boolean emailPrivate){
            this.emailPrivate = emailPrivate;
        }

        public void setIsPhoneNumberPrivate (Boolean phoneNumberPrivate){
            this.phoneNumberPrivate = phoneNumberPrivate;
        }

        public boolean isEmailPrivate () {
            return emailPrivate;
        }

        public Boolean isPhoneNumberPrivate () {
            return phoneNumberPrivate;
        }

}