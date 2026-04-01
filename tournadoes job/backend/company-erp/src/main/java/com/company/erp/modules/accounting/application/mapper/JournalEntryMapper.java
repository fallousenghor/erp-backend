package com.company.erp.modules.accounting.application.mapper;

import com.company.erp.modules.accounting.application.dto.request.CreateJournalEntryRequest;
import com.company.erp.modules.accounting.application.dto.response.JournalEntryResponse;
import com.company.erp.modules.accounting.domain.model.JournalEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    JournalEntry toEntity(CreateJournalEntryRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    JournalEntryResponse toResponse(JournalEntry entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    void updateEntity(CreateJournalEntryRequest request, @MappingTarget JournalEntry entity);
}

