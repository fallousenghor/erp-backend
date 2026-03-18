package com.company.erp.modules.accounting.application.service;

import com.company.erp.modules.accounting.application.dto.request.CreateJournalEntryRequest;
import com.company.erp.modules.accounting.application.dto.response.JournalEntryResponse;
import com.company.erp.modules.accounting.application.mapper.JournalEntryMapper;
import com.company.erp.modules.accounting.domain.model.JournalEntry;
import com.company.erp.modules.accounting.domain.repository.JournalEntryRepository;
import jakarta.persistence.EntityNotFoundException;


import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class JournalEntryService {

    private final JournalEntryRepository repository;
    private final JournalEntryMapper mapper;

    public JournalEntryResponse create(CreateJournalEntryRequest request) {
        JournalEntry entity = mapper.toEntity(request);
        entity = repository.save(entity);
        log.info("Journal entry created: {}", entity.getReference());
        return mapper.toResponse(entity);
    }

    public JournalEntryResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Journal entry not found: " + id));
    
    }

    public PageResponse<JournalEntryResponse> findAll(String reference, String accountCode, Pageable pageable) {
        // Implement specification filtering
        var page = repository.findAll(pageable).map(mapper::toResponse);
        return PageResponse.from(page);
    }

    public JournalEntryResponse update(UUID id, CreateJournalEntryRequest request) {
        JournalEntry entity = findOrThrow(id);
        mapper.updateEntity(request, entity);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    public void delete(UUID id) {
        JournalEntry entity = findOrThrow(id);
        repository.delete(entity);
    }

    private JournalEntry findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Journal entry not found: " + id));
    }
}

