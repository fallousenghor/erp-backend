package com.ceremonie.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    
    @NotBlank
    @Column(name = "member_number", unique = true, nullable = false)
    private String memberNumber; // Numéro d'identification unique (auto-généré)
    
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(name = "emergency_contact")
    private String emergencyContact;
    
    @Column(name = "emergency_phone")
    private String emergencyPhone;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Badge badge;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contribution> contributions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MaterialLoan> materialLoans = new ArrayList<>();
}