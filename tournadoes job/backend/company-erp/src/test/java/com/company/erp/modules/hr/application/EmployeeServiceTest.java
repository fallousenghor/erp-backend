package com.company.erp.modules.hr.application;

import com.company.erp.modules.hr.application.dto.request.CreateEmployeeRequest;
import com.company.erp.modules.hr.application.dto.response.EmployeeResponse;
import com.company.erp.modules.hr.application.mapper.EmployeeMapper;
import com.company.erp.modules.hr.application.service.EmployeeService;
import com.company.erp.modules.hr.domain.model.Employee;
import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import com.company.erp.modules.hr.domain.repository.EmployeeRepository;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeMapper employeeMapper;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks private EmployeeService employeeService;

    @Test
    void create_whenEmailAlreadyExists_throwsBusinessException() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "John", "Doe", "john@company.com", null, null,
                LocalDate.now(), UUID.randomUUID(), null,
                null, null,
                BigDecimal.valueOf(500000), "XOF",
                Contract.ContractType.CDI, LocalDate.now(), null, null);

        when(employeeRepository.existsByEmail("john@company.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> employeeService.create(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void create_withValidData_savesEmployee() {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "Jane", "Smith", "jane@company.com", null, null,
                LocalDate.now(), UUID.randomUUID(), null,
                null, null,
                BigDecimal.valueOf(600000), "XOF",
                Contract.ContractType.CDI, LocalDate.now(), null, null);

        Employee savedEmployee = mock(Employee.class);
        when(savedEmployee.getId()).thenReturn(UUID.randomUUID());
        when(savedEmployee.getEmployeeNumber()).thenReturn("EMP-2025-00001");
        when(savedEmployee.getFullName()).thenReturn("Jane Smith");

        when(employeeRepository.existsByEmail(any())).thenReturn(false);
        when(employeeRepository.save(any())).thenReturn(savedEmployee);
        when(employeeMapper.toResponse(any())).thenReturn(mock(EmployeeResponse.class));

        assertDoesNotThrow(() -> employeeService.create(request));
        verify(employeeRepository).save(any());
        verify(eventPublisher).publish(any());
    }
}
