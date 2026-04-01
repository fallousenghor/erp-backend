
package com.ceremony.ceremony_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(nullable = false)
    private String url; // Cloudinary URL
    
    @Column(nullable = false)
    private String publicId; // Cloudinary public ID
    
    @Column(nullable = false)
    private String type; // IMAGE, VIDEO, AUDIO
    
    @ManyToOne
    @JoinColumn(name = "edition_id")
    private Edition edition;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

