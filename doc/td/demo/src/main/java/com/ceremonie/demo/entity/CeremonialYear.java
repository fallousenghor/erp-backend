package com.ceremonie.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ceremonial_years")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CeremonialYear extends BaseEntity {
    
    @NotNull
    @Column(unique = true, nullable = false)
    private Integer year;
    
    @Column(nullable = false)
    private String theme; // Thème de l'année
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private Boolean active = false; // Une seule année active à la fois
    
    @Column(name = "initial_budget")
    private BigDecimal initialBudget;
    
    @Column(name = "total_income")
    private BigDecimal totalIncome = BigDecimal.ZERO;
    
    @Column(name = "total_expense")
    private BigDecimal totalExpense = BigDecimal.ZERO;
    
    @Column(name = "total_contributions")
    private BigDecimal totalContributions = BigDecimal.ZERO;
    
    @OneToMany(mappedBy = "ceremonialYear", cascade = CascadeType.ALL)
    private List<Media> medias = new ArrayList<>();
    
    @OneToMany(mappedBy = "ceremonialYear", cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();
    
    @OneToMany(mappedBy = "ceremonialYear", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}
