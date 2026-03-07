package com.company.erp.modules.hr.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Contract Value Object — embedded in Employee.
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    public enum ContractType { CDI, CDD, FREELANCE, INTERNSHIP, PART_TIME }

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false, length = 20)
    private ContractType contractType;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "contract_end_date")
    private LocalDate endDate;

    public static Contract of(ContractType type, LocalDate startDate, LocalDate endDate) {
        if (type == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Contract type is required");
        }
        if (startDate == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Contract start date is required");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Contract end date cannot be before start date");
        }
        if (type == ContractType.CDI && endDate != null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "CDI contracts cannot have an end date");
        }
        return new Contract(type, startDate, endDate);
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) &&
                (endDate == null || !today.isAfter(endDate));
    }
}
