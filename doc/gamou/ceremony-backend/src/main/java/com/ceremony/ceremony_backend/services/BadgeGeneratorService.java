package com.ceremony.ceremony_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ceremony.ceremony_backend.repository.MemberRepository;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class BadgeGeneratorService {
    
    private final MemberRepository memberRepository;
    
    /**
     * Génère un numéro de badge unique au format: CRM-2025-00001
     */
    public String generateBadgeNumber() {
        int currentYear = Year.now().getValue();
        String yearPrefix = "DTD-" + currentYear + "-";
        
        // Récupérer le dernier numéro de badge pour cette année
        Integer maxNumber = memberRepository.findMaxBadgeNumberForYear(yearPrefix + "%");
        
        int nextNumber = (maxNumber != null) ? maxNumber + 1 : 1;
        
        return String.format("%s%05d", yearPrefix, nextNumber);
    }
}