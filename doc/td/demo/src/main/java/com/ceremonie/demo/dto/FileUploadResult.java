package com.ceremonie.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResult {
    private String publicId;
    private String secureUrl;
    private String url;
    private String format;
    private Integer width;
    private Integer height;
    private Long bytes;
}
