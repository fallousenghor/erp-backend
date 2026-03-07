package com.company.erp.modules.inventory.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

/**
 * AssetCode Value Object — format: AST-YYYY-NNNNN
 */
public record AssetCode(String value) {

    public AssetCode {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Asset code cannot be blank");
        }
        value = value.trim().toUpperCase();
        if (!value.matches("^[A-Z0-9\\-]{4,20}$")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Asset code must be 4-20 alphanumeric/dash characters");
        }
    }

    public static AssetCode generate(String category) {
        String year = String.valueOf(java.time.LocalDate.now().getYear());
        String seq  = String.format("%05d", (int)(Math.random() * 99999));
        String prefix = category != null ? category.substring(0, Math.min(3, category.length())).toUpperCase() : "AST";
        return new AssetCode(prefix + "-" + year + "-" + seq);
    }

    @Override
    public String toString() { return value; }
}
