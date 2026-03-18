package com.company.erp.modules.accounting.application.mapper;

import com.company.erp.modules.accounting.application.dto.request.CreateJournalEntryRequest;
import com.company.erp.modules.accounting.application.dto.response.JournalEntryResponse;
import com.company.erp.modules.accounting.domain.model.JournalEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {
    
    JournalEntry toEntity(CreateJournalEntryRequest request);
    
    JournalEntryResponse toResponse(JournalEntry entity);
    
 @Mapping(target = "id", ignore = true)
    void updateEntity(CreateJournalEntryRequest request, @MappingTarget JournalEntry entity);
    

}

