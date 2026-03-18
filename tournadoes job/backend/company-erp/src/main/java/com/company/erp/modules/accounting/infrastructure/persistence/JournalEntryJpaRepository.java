package com.company.erp.modules.accounting.infrastructure.persistence;

import com.company.erp.modules.accounting.domain.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JournalEntryJpaRepository extends JpaRepository<JournalEntry, UUID> {
}
