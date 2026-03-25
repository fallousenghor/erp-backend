package com.company.erp.modules.organization.application.mapper;

import com.company.erp.modules.organization.domain.model.Department;
import com.company.erp.modules.organization.domain.model.DepartmentHead;
import com.company.erp.modules.organization.domain.model.Position;
import com.company.erp.modules.organization.application.dto.response.DepartmentResponse;
import com.company.erp.modules.organization.application.dto.response.DepartmentDetailResponse;
import com.company.erp.modules.organization.application.dto.response.DepartmentStatsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "currentHeadName", expression = "java(resolveHeadName(department))")
    @Mapping(target = "positionCount", expression = "java(department.getPositions() != null ? department.getPositions().size() : 0)")
    @Mapping(target = "budget", expression = "java(department.getBudget() != null ? department.getBudget() : BigDecimal.ZERO)")
    DepartmentResponse toResponse(Department department);

    @Mapping(target = "budget", expression = "java(mapBudget(department))")
    @Mapping(target = "currentHead", expression = "java(mapCurrentHead(department))")
    @Mapping(target = "headHistory", expression = "java(mapHeadHistory(department))")
    @Mapping(target = "employeeCount", expression = "java(0)")  // Will be populated by service
    @Mapping(target = "positionCount", expression = "java(department.getPositions() != null ? department.getPositions().size() : 0)")
    DepartmentDetailResponse toDetailResponse(Department department);

    @Mapping(target = "budgetUtilizationPercent", expression = "java(calculateBudgetUtilization(department))")
    @Mapping(target = "openPositions", expression = "java(countOpenPositions(department))")
    DepartmentStatsResponse toStatsResponse(Department department);

    default String resolveHeadName(Department department) {
        DepartmentHead head = department.getCurrentHead();
        return head != null ? head.getEmployeeName() : null;
    }

    default DepartmentDetailResponse.BudgetInfo mapBudget(Department department) {
        return new DepartmentDetailResponse.BudgetInfo(
                department.getBudget() != null ? department.getBudget() : BigDecimal.ZERO,
                BigDecimal.ZERO, // spent - to be calculated from expenses
                department.remainingBudget() != null ? department.remainingBudget() : BigDecimal.ZERO,
                "XOF" // CFA Franc - adjust as needed
        );
    }

    default DepartmentDetailResponse.CurrentHeadInfo mapCurrentHead(Department department) {
        DepartmentHead head = department.getCurrentHead();
        if (head == null) return null;
        return new DepartmentDetailResponse.CurrentHeadInfo(
                head.getEmployeeId(),
                head.getEmployeeName(),
                head.getStartDate()
        );
    }

    default List<DepartmentDetailResponse.HeadHistoryItem> mapHeadHistory(Department department) {
        if (department.getHeads() == null) {
            return new ArrayList<>();
        }
        return department.getHeads().stream()
                .map(h -> new DepartmentDetailResponse.HeadHistoryItem(
                        h.getEmployeeId(),
                        h.getEmployeeName(),
                        h.getStartDate(),
                        h.getEndDate(),
                        h.isCurrent()
                ))
                .toList();
    }

    default BigDecimal calculateBudgetUtilization(Department department) {
        if (department.getBudget() == null || department.getBudget().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        // Placeholder - in real scenario, calculate from actual expenses
        return BigDecimal.ZERO;
    }

    default int countOpenPositions(Department department) {
        if (department.getPositions() == null) return 0;
        return (int) department.getPositions().stream()
                .filter(Position::isActive)
                .count();
    }
}
