package com.ceremonie.demo.services.impl;

import com.ceremonie.demo.dto.response.BadgeResponse;
import com.ceremonie.demo.entity.Badge;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.BadgeRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.services.interfaces.BadgeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public BadgeResponse generateBadge(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));

        // Vérifier si le membre a déjà un badge
        if (badgeRepository.findByMember(member).isPresent()) {
            throw new IllegalStateException("Le membre a déjà un badge");
        }

        String badgeNumber = "BADGE" + member.getMemberNumber();
        String qrCodeData = generateQRCodeData(member);

        Badge badge = Badge.builder()
                .member(member)
                .badgeNumber(badgeNumber)
                .qrCodeData(qrCodeData)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(5))
                .active(true)
                .build();

        Badge savedBadge = badgeRepository.save(badge);
        
        // TODO: Générer le PDF du badge
        // savedBadge.setPdfUrl(generateBadgePdfUrl(savedBadge));
        // badgeRepository.save(savedBadge);

        return mapToResponse(savedBadge);
    }

    @Override
    public BadgeResponse getBadgeByMemberId(Long memberId) {
        Badge badge = badgeRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Badge non trouvé"));
        return mapToResponse(badge);
    }

    @Override
    public byte[] generateBadgePdf(Long memberId) {
        // TODO: Implémenter la génération de PDF avec iText
        // Cette méthode créera un PDF avec:
        // - Photo du membre
        // - Nom complet
        // - Numéro de membre
        // - QR Code
        // - Logo de l'organisation
        return new byte[0];
    }

    @Override
    public String generateQRCode(String data) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du QR Code", e);
        }
    }

    private String generateQRCodeData(Member member) {
        return String.format("MEMBER:%s|NAME:%s %s|PHONE:%s",
                member.getMemberNumber(),
                member.getFirstName(),
                member.getLastName(),
                member.getPhoneNumber());
    }

    private BadgeResponse mapToResponse(Badge badge) {
        return BadgeResponse.builder()
                .id(badge.getId())
                .badgeNumber(badge.getBadgeNumber())
                .issueDate(badge.getIssueDate())
                .expiryDate(badge.getExpiryDate())
                .pdfUrl(badge.getPdfUrl())
                .active(badge.getActive())
                .build();
    }
}