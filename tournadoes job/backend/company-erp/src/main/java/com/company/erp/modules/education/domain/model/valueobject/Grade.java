package com.company.erp.modules.education.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Grade Value Object — score out of 20, with letter grade derivation.
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "max_score", precision = 5, scale = 2)
    private BigDecimal maxScore;

    public static Grade of(BigDecimal score, BigDecimal maxScore) {
        if (score == null || maxScore == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Score and max score cannot be null");
        }
        if (maxScore.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Max score must be greater than zero");
        }
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(maxScore) > 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Score must be between 0 and " + maxScore);
        }
        return new Grade(
                score.setScale(2, RoundingMode.HALF_UP),
                maxScore.setScale(2, RoundingMode.HALF_UP));
    }

    /** Returns score normalized to 20. */
    public BigDecimal outOf20() {
        return score.multiply(new BigDecimal("20"))
                .divide(maxScore, 2, RoundingMode.HALF_UP);
    }

    /** Returns percentage score. */
    public BigDecimal percentage() {
        return score.multiply(new BigDecimal("100"))
                .divide(maxScore, 2, RoundingMode.HALF_UP);
    }

    public String letterGrade() {
        double pct = percentage().doubleValue();
        if (pct >= 90) return "A";
        if (pct >= 80) return "B";
        if (pct >= 70) return "C";
        if (pct >= 60) return "D";
        return "F";
    }

    public boolean isPassing() {
        return percentage().compareTo(new BigDecimal("60")) >= 0;
    }

    @Override
    public String toString() {
        return score.toPlainString() + "/" + maxScore.toPlainString()
                + " (" + letterGrade() + ")";
    }
}
