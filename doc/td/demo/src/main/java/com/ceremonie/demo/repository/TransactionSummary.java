package com.ceremonie.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ceremonie.demo.enums.TransactionType;

public interface TransactionSummary {
    Long getId();
    LocalDate getTransactionDate();
    TransactionType getType();
    BigDecimal getAmount();
    String getDescription();
    String getCategory();
}
