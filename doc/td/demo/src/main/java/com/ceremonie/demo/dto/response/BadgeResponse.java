package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponse {
    private Long id;
    private String badgeNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String pdfUrl;
    private Boolean active;
}
