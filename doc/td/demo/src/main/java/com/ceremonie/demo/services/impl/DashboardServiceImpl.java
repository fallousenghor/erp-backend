package com.ceremonie.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ceremonie.demo.dto.response.DashboardStatsResponse;
import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.ContributionRepository;
import com.ceremonie.demo.repository.EventRepository;
import com.ceremonie.demo.repository.MaterialLoanRepository;
import com.ceremonie.demo.repository.MediaRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.repository.UserRepository;
import com.ceremonie.demo.services.interfaces.DashboardService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final CeremonialYearRepository ceremonialYearRepository;
    private final EventRepository eventRepository;
    private final ContributionRepository contributionRepository;
    private final MaterialLoanRepository materialLoanRepository;
    private final MediaRepository mediaRepository;

    @Override
    public DashboardStatsResponse getDashboardStats() {
        // Statistiques membres
        Long totalMembers = memberRepository.count();
        Long activeMembers = memberRepository.countActiveMembers();

        // Statistiques utilisateurs
        Long totalUsers = userRepository.count();

        // Année active
        CeremonialYear activeYear = ceremonialYearRepository.findActiveCeremonialYear().orElse(null);
        
        BigDecimal currentBalance = BigDecimal.ZERO;
        BigDecimal totalIncomeThisYear = BigDecimal.ZERO;
        BigDecimal totalExpenseThisYear = BigDecimal.ZERO;
        Long upcomingEvents = 0L;
        Long unpaidContributions = 0L;
        Long totalMedias = 0L;
        String activeCeremonialYear = "Aucune";

        if (activeYear != null) {
            currentBalance = activeYear.getTotalIncome().subtract(activeYear.getTotalExpense());
            totalIncomeThisYear = activeYear.getTotalIncome();
            totalExpenseThisYear = activeYear.getTotalExpense();
            upcomingEvents = eventRepository.countEventsByYear(activeYear.getId());
            unpaidContributions = contributionRepository.countUnpaidContributionsByYear(activeYear.getId());
            totalMedias = mediaRepository.countMediasByYear(activeYear.getId());
            activeCeremonialYear = activeYear.getYear().toString();
        }

        // Emprunts actifs
        Long activeMaterialLoans = (long) materialLoanRepository.findActiveLoans().size();

        return DashboardStatsResponse.builder()
                .totalMembers(totalMembers)
                .activeMembers(activeMembers)
                .totalUsers(totalUsers)
                .currentBalance(currentBalance)
                .totalIncomeThisYear(totalIncomeThisYear)
                .totalExpenseThisYear(totalExpenseThisYear)
                .upcomingEvents(upcomingEvents)
                .unpaidContributions(unpaidContributions)
                .activeMaterialLoans(activeMaterialLoans)
                .totalMedias(totalMedias)
                .activeCeremonialYear(activeCeremonialYear)
                .build();
    }
}
