package com.company.erp.modules.finance.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class ExpenseRecordedEvent extends BaseDomainEvent {
    private final UUID expenseId;
    private final String title;
    private final BigDecimal amount;

    public ExpenseRecordedEvent(UUID expenseId, String title, BigDecimal amount) {
        super("EXPENSE_RECORDED");
        this.expenseId = expenseId;
        this.title = title;
        this.amount = amount;
    }
}
