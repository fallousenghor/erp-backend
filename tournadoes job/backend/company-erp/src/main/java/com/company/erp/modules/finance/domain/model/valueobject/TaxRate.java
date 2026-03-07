package com.company.erp.modules.finance.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * TaxRate Value Object — percentage between 0 and 100.
 */
public record TaxRate(BigDecimal percentage) {

    public TaxRate {
        if (percentage == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Tax rate cannot be null");
        }
        if (percentage.compareTo(BigDecimal.ZERO) < 0
                || percentage.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Tax rate must be between 0 and 100");
        }
        percentage = percentage.setScale(2, RoundingMode.HALF_UP);
    }

    public static TaxRate of(double percentage) {
        return new TaxRate(BigDecimal.valueOf(percentage));
    }

    public static TaxRate zero() {
        return new TaxRate(BigDecimal.ZERO);
    }

    public BigDecimal applyTo(BigDecimal amount) {
        return amount.multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
