package com.ceremonie.demo.services.interfaces;


import java.time.LocalDate;
import java.util.List;

import com.ceremonie.demo.dto.request.CreateTransactionRequest;
import com.ceremonie.demo.dto.response.FinancialReportResponse;
import com.ceremonie.demo.dto.response.TransactionResponse;
import com.ceremonie.demo.enums.TransactionType;

public interface TransactionService {
    TransactionResponse createTransaction(CreateTransactionRequest request);
    TransactionResponse getTransactionById(Long id);
    List<TransactionResponse> getTransactionsByYear(Long yearId);
    List<TransactionResponse> getTransactionsByActiveYear();
    List<TransactionResponse> getTransactionsByType(TransactionType type);
    List<TransactionResponse> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate);
    FinancialReportResponse getFinancialReport(Long yearId);
    void deleteTransaction(Long id);
}