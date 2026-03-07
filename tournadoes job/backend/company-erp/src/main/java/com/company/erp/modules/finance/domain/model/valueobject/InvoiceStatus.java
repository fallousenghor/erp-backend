package com.company.erp.modules.finance.domain.model.valueobject;

/**
 * Invoice lifecycle status.
 * Valid transitions:
 *   DRAFT → SENT → PAID
 *   DRAFT → CANCELLED
 *   SENT  → CANCELLED
 */
public enum InvoiceStatus {
    DRAFT, SENT, PAID, CANCELLED;

    public boolean canTransitionTo(InvoiceStatus next) {
        return switch (this) {
            case DRAFT      -> next == SENT || next == CANCELLED;
            case SENT       -> next == PAID || next == CANCELLED;
            case PAID       -> false;
            case CANCELLED  -> false;
        };
    }
}
