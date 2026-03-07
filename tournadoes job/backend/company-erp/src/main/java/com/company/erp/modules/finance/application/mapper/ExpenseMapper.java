package com.company.erp.modules.finance.application.mapper;

import com.company.erp.modules.finance.application.dto.response.ExpenseResponse;
import com.company.erp.modules.finance.domain.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "amount",   source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    ExpenseResponse toResponse(Expense expense);
}
