package com.company.erp.modules.hr.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Salary Value Object — embedded in Employee.
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Salary {

    @Column(name = "base_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    public static Salary of(BigDecimal amount, String currency) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Salary cannot be negative");
        }
        if (currency == null || currency.isBlank() || currency.length() != 3) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Currency must be a 3-letter ISO code (e.g. XOF, EUR, USD)");
        }
        return new Salary(amount, currency.toUpperCase());
    }

    public Salary increase(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Increase amount must be positive");
        }
        return new Salary(this.baseSalary.add(amount), this.currency);
    }
}
