package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.domain.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    private final LeaveRequestJpaRepository jpaRepository;

    @Override public LeaveRequest save(LeaveRequest lr)                          { return jpaRepository.save(lr); }
    @Override public Optional<LeaveRequest> findById(UUID id)                   { return jpaRepository.findById(id); }
    @Override public Page<LeaveRequest> findByEmployeeId(UUID id, Pageable pg)  { return jpaRepository.findByEmployeeId(id, pg); }
    @Override public Page<LeaveRequest> findAll(Pageable pageable)              { return jpaRepository.findAll(pageable); }

    @Override
    public Page<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable);
    }
}
