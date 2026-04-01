package com.ceremonie.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ceremonie.demo.enums.MaterialStatus;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "reference_number", unique = true)
    private String referenceNumber;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialStatus status = MaterialStatus.BON_ETAT;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "location")
    private String location; // Où est stocké le matériel
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    private List<MaterialLoan> loans = new ArrayList<>();
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}