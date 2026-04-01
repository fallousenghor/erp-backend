package com.ceremonie.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge extends BaseEntity {
    
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;
    
    @Column(name = "badge_number", unique = true, nullable = false)
    private String badgeNumber;
    
    @Column(name = "qr_code_data")
    private String qrCodeData; // Données encodées dans le QR code
    
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "pdf_url")
    private String pdfUrl; // URL du badge PDF généré
    
    @Column(nullable = false)
    private Boolean active = true;
}