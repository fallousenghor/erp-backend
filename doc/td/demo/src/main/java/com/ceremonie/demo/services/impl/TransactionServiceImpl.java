package com.ceremonie.demo.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremonie.demo.dto.request.CreateTransactionRequest;
import com.ceremonie.demo.dto.response.FinancialReportResponse;
import com.ceremonie.demo.dto.response.TransactionResponse;
import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.entity.Transaction;
import com.ceremonie.demo.entity.User;
import com.ceremonie.demo.enums.TransactionType;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.ContributionRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.repository.TransactionRepository;
import com.ceremonie.demo.repository.UserRepository;
import com.ceremonie.demo.services.interfaces.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CeremonialYearRepository ceremonialYearRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ContributionRepository contributionRepository;

    @Override
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        CeremonialYear year = ceremonialYearRepository.findById(request.getCeremonialYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));

        Transaction transaction = Transaction.builder()
                .ceremonialYear(year)
                .transactionDate(request.getTransactionDate())
                .type(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .category(request.getCategory())
                .paymentMethod(request.getPaymentMethod())
                .referenceNumber(request.getReferenceNumber())
                .notes(request.getNotes())
                .build();

        if (request.getMemberId() != null) {
            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));
            transaction.setMember(member);
        }

        // Obtenir l'utilisateur connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            transaction.setRecordedBy(user);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Mettre à jour les totaux de l'année
        updateYearTotals(year);

        return mapToResponse(savedTransaction);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée"));
        return mapToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getTransactionsByYear(Long yearId) {
        return transactionRepository.findByCeremonialYearId(yearId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByActiveYear() {
        return transactionRepository.findByActiveCeremonialYear().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateRange(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FinancialReportResponse getFinancialReport(Long yearId) {
        CeremonialYear year = ceremonialYearRepository.findById(yearId)
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));

        BigDecimal totalIncome = transactionRepository.getTotalIncomeByYear(yearId);
        BigDecimal totalExpense = transactionRepository.getTotalExpenseByYear(yearId);
        BigDecimal totalContributions = contributionRepository.getTotalPaidByYear(yearId);

        Long paidCount = contributionRepository.countPaidContributionsByYear(yearId);
        Long unpaidCount = contributionRepository.countUnpaidContributionsByYear(yearId);

        // Dépenses par catégorie
        List<Object[]> expensesByCat = transactionRepository.getExpensesByCategory(yearId);
        Map<String, BigDecimal> expensesByCategory = new HashMap<>();
        for (Object[] row : expensesByCat) {
            expensesByCategory.put((String) row[0], (BigDecimal) row[1]);
        }

        // Transactions récentes
        List<TransactionResponse> recentTransactions = transactionRepository
                .findByCeremonialYearId(yearId).stream()
                .limit(10)
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return FinancialReportResponse.builder()
                .ceremonialYearId(yearId)
                .year(year.getYear())
                .totalIncome(totalIncome != null ? totalIncome : BigDecimal.ZERO)
                .totalExpense(totalExpense != null ? totalExpense : BigDecimal.ZERO)
                .totalContributions(totalContributions != null ? totalContributions : BigDecimal.ZERO)
                .balance((totalIncome != null ? totalIncome : BigDecimal.ZERO)
                        .subtract(totalExpense != null ? totalExpense : BigDecimal.ZERO))
                .initialBudget(year.getInitialBudget())
                .transactionCount(transactionRepository.countTransactionsByYear(yearId))
                .contributionCount(paidCount + unpaidCount)
                .paidContributionCount(paidCount)
                .unpaidContributionCount(unpaidCount)
                .expensesByCategory(expensesByCategory)
                .recentTransactions(recentTransactions)
                .build();
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction non trouvée"));
        transaction.setDeleted(true);
        transactionRepository.save(transaction);
        
        // Mettre à jour les totaux
        updateYearTotals(transaction.getCeremonialYear());
    }

    @Transactional
    private void updateYearTotals(CeremonialYear year) {
        BigDecimal totalIncome = transactionRepository.getTotalIncomeByYear(year.getId());
        BigDecimal totalExpense = transactionRepository.getTotalExpenseByYear(year.getId());
        BigDecimal totalContributions = contributionRepository.getTotalPaidByYear(year.getId());

        year.setTotalIncome(totalIncome != null ? totalIncome : BigDecimal.ZERO);
        year.setTotalExpense(totalExpense != null ? totalExpense : BigDecimal.ZERO);
        year.setTotalContributions(totalContributions != null ? totalContributions : BigDecimal.ZERO);

        ceremonialYearRepository.save(year);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .ceremonialYearId(transaction.getCeremonialYear().getId())
                .ceremonialYear(transaction.getCeremonialYear().getYear())
                .transactionDate(transaction.getTransactionDate())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .category(transaction.getCategory())
                .paymentMethod(transaction.getPaymentMethod())
                .referenceNumber(transaction.getReferenceNumber())
                .memberName(transaction.getMember() != null ? 
                        transaction.getMember().getFirstName() + " " + transaction.getMember().getLastName() : null)
                .recordedByName(transaction.getRecordedBy() != null ?
                        transaction.getRecordedBy().getFirstName() + " " + transaction.getRecordedBy().getLastName() : null)
                .receiptUrl(transaction.getReceiptUrl())
                .build();
    }
}
