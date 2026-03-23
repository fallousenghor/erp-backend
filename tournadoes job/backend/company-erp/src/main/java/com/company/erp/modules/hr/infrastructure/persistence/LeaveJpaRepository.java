package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LeaveJpaRepository extends JpaRepository<LeaveRequest, UUID> {

  @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = :status")
  List<LeaveRequest> findPending(@Param("status") String status);

}
