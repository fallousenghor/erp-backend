package com.ceremonie.demo.services.interfaces;

import com.ceremonie.demo.dto.response.BadgeResponse;

public interface BadgeService {
    BadgeResponse generateBadge(Long memberId);
    BadgeResponse getBadgeByMemberId(Long memberId);
    byte[] generateBadgePdf(Long memberId);
    String generateQRCode(String data);
}