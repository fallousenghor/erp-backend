package com.company.erp.modules.accounting.domain.repository;

import com.company.erp.modules.accounting.domain.model.JournalEntry;
import com.company.erp.modules.accounting.infrastructure.persistence.JournalEntryJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JournalEntryJpaRepository, JpaRepository<JournalEntry, UUID>, JpaSpecificationExecutor<JournalEntry> {
}

