package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.ceremonie.demo.enums.MediaType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaResponse {
    private Long id;
    private Long ceremonialYearId;
    private Integer ceremonialYear;
    private String title;
    private String description;
    private MediaType type;
    private String fileUrl;
    private String thumbnailUrl;
    private Long fileSize;
    private Integer duration;
    private String uploadedByName;
    private String tags;
    private LocalDateTime createdAt;
}
