package com.company.erp.modules.finance.application.mapper;

import com.company.erp.modules.finance.application.dto.response.InvoiceResponse;
import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.model.InvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "items", source = "items")
    InvoiceResponse toResponse(Invoice invoice);

    InvoiceResponse.ItemResponse toItemResponse(InvoiceItem item);
}
