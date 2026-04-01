package com.ceremonie.demo.entity;

import com.ceremonie.demo.enums.MediaType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "ceremonial_year_id", nullable = false)
    private CeremonialYear ceremonialYear;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;
    
    @Column(name = "file_url", nullable = false)
    private String fileUrl;
    
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @Column(name = "file_size")
    private Long fileSize; // En bytes
    
    @Column(name = "duration")
    private Integer duration; // En secondes (pour vidéo/audio)
    
    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;
    
    @Column(columnDefinition = "TEXT")
    private String tags; // Tags séparés par virgules
}