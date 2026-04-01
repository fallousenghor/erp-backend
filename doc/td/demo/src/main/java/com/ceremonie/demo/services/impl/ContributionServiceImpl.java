package com.ceremonie.demo.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremonie.demo.dto.request.CreateContributionRequest;
import com.ceremonie.demo.dto.response.ContributionResponse;
import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Contribution;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.entity.Transaction;
import com.ceremonie.demo.enums.ContributionStatus;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.ContributionRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.repository.TransactionRepository;
import com.ceremonie.demo.services.interfaces.ContributionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContributionServiceImpl implements ContributionService {

    private final ContributionRepository contributionRepository;
    private final MemberRepository memberRepository;
    private final CeremonialYearRepository ceremonialYearRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public ContributionResponse createContribution(CreateContributionRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));
        
        CeremonialYear year = ceremonialYearRepository.findById(request.getCeremonialYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));

        Contribution contribution = Contribution.builder()
                .member(member)
                .ceremonialYear(year)
                .expectedAmount(request.getExpectedAmount())
                .paidAmount(BigDecimal.ZERO)
                .status(ContributionStatus.IMPAYE)
                .dueDate(request.getDueDate())
                .notes(request.getNotes())
                .build();

        Contribution savedContribution = contributionRepository.save(contribution);
        return mapToResponse(savedContribution);
    }

    @Override
    @Transactional
    public ContributionResponse updateContribution(Long id, CreateContributionRequest request) {
        Contribution contribution = contributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotisation non trouvée"));

        contribution.setExpectedAmount(request.getExpectedAmount());
        contribution.setDueDate(request.getDueDate());
        contribution.setNotes(request.getNotes());

        Contribution updatedContribution = contributionRepository.save(contribution);
        return mapToResponse(updatedContribution);
    }

    @Override
    public ContributionResponse getContributionById(Long id) {
        Contribution contribution = contributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotisation non trouvée"));
        return mapToResponse(contribution);
    }

    @Override
    public List<ContributionResponse> getAllContributions() {
        return contributionRepository.findAll().stream()
                .filter(contribution -> !Boolean.TRUE.equals(contribution.getDeleted()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContributionResponse> getContributionsByMember(Long memberId) {
        return contributionRepository.findByMemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContributionResponse> getContributionsByYear(Long yearId) {
        return contributionRepository.findByCeremonialYearId(yearId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContributionResponse> getUnpaidContributions() {
        return contributionRepository.findUnpaidContributions().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsPaid(Long contributionId, Long transactionId) {
        Contribution contribution = contributionRepository.findById(contributionId)
                .orElseThrow(() -> new ResourceNotFoundException("Cotisation non trouvée"));
        
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée"));

        contribution.setPaidAmount(transaction.getAmount());
        contribution.setPaymentDate(LocalDate.now());
        contribution.setTransaction(transaction);
        
        // Déterminer le statut
        if (contribution.getPaidAmount().compareTo(contribution.getExpectedAmount()) >= 0) {
            contribution.setStatus(ContributionStatus.PAYE);
        } else if (contribution.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            contribution.setStatus(ContributionStatus.PARTIEL);
        }

        contributionRepository.save(contribution);
    }

    @Override
    @Transactional
    public void deleteContribution(Long id) {
        Contribution contribution = contributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotisation non trouvée"));
        contribution.setDeleted(true);
        contributionRepository.save(contribution);
    }

    private ContributionResponse mapToResponse(Contribution contribution) {
        BigDecimal remainingAmount = contribution.getExpectedAmount()
                .subtract(contribution.getPaidAmount());

        return ContributionResponse.builder()
                .id(contribution.getId())
                .memberId(contribution.getMember().getId())
                .memberName(contribution.getMember().getFirstName() + " " + 
                        contribution.getMember().getLastName())
                .memberNumber(contribution.getMember().getMemberNumber())
                .ceremonialYearId(contribution.getCeremonialYear().getId())
                .ceremonialYear(contribution.getCeremonialYear().getYear())
                .expectedAmount(contribution.getExpectedAmount())
                .paidAmount(contribution.getPaidAmount())
                .remainingAmount(remainingAmount)
                .status(contribution.getStatus())
                .dueDate(contribution.getDueDate())
                .paymentDate(contribution.getPaymentDate())
                .notes(contribution.getNotes())
                .build();
    }
}
