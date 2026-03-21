package com.company.erp.modules.hr.application.service;

import com.company.erp.modules.hr.application.dto.request.CreateEmployeeRequest;
import com.company.erp.modules.hr.application.dto.response.EmployeeResponse;
import com.company.erp.modules.hr.application.mapper.EmployeeMapper;
import com.company.erp.modules.hr.domain.event.EmployeeCreatedEvent;
import com.company.erp.modules.hr.domain.event.EmployeeTerminatedEvent;
import com.company.erp.modules.hr.domain.model.Employee;
import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import com.company.erp.modules.hr.domain.model.valueobject.Salary;
import com.company.erp.modules.hr.domain.repository.EmployeeRepository;
import com.company.erp.modules.hr.infrastructure.persistence.specification.EmployeeSpecification;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import com.company.erp.shared.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DomainEventPublisher eventPublisher;
    private final QRCodeService qrCodeService;

    @Auditable(action = "CREATE_EMPLOYEE", entity = "Employee")
    @PreAuthorize("hasPermission(null, 'employee:create')")
    public EmployeeResponse create(CreateEmployeeRequest request) {
        // Check for duplicate email
        if (employeeRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    "Email already registered: " + request.email());
        }

        String employeeNumber = generateEmployeeNumber();

        // Handle nullable departmentId
        UUID deptId = request.departmentId();
        String deptName = request.departmentName();

        Employee employee = Employee.builder()
                .employeeNumber(employeeNumber)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email().toLowerCase())
                .phone(request.phone())
                .birthDate(request.birthDate())
                .hireDate(request.hireDate() != null ? request.hireDate() : LocalDate.now())
                .departmentId(deptId)
                .departmentName(deptName)
                .positionId(request.positionId())
                .positionTitle(request.positionTitle())
                .salary(Salary.of(
                        request.baseSalary() != null ? request.baseSalary() : BigDecimal.ZERO,
                        request.currency() != null ? request.currency() : "XOF"))
                .photoUrl(request.photoUrl())
                .build();
        
        // Handle contract - use defaults if not provided
        if (request.contractType() != null && request.contractStartDate() != null) {
            employee.setContract(Contract.of(
                    request.contractType(),
                    request.contractStartDate(),
                    request.contractEndDate()));
        } else if (request.contractType() != null) {
            // Use today's date as start date if not provided
            employee.setContract(Contract.of(
                    request.contractType(),
                    LocalDate.now(),
                    request.contractEndDate()));
        } else {
            // Default to CDI with today's date
            employee.setContract(Contract.of(
                    Contract.ContractType.CDI,
                    LocalDate.now(),
                    null));
        }

        // Generate and upload QR code
        String qrCodeUrl = qrCodeService.generateAndUploadQRCode(
                employeeNumber,
                request.firstName(),
                request.lastName(),
                request.email().toLowerCase()
        );
        if (qrCodeUrl != null) {
            employee.setQrCodeUrl(qrCodeUrl);
        }

        employee = employeeRepository.save(employee);
        eventPublisher.publish(new EmployeeCreatedEvent(
                employee.getId(), employee.getEmployeeNumber(), employee.getFullName()));

        log.info("Employee created: [{}] {}", employee.getEmployeeNumber(), employee.getFullName());
        return employeeMapper.toResponse(employee);
    }

    @Auditable(action = "UPDATE_EMPLOYEE", entity = "Employee")
    @PreAuthorize("hasPermission(null, 'employee:update')")
    public EmployeeResponse update(UUID id, com.company.erp.modules.hr.application.dto.request.UpdateEmployeeRequest request) {
        Employee employee = findOrThrow(id);

        if (request.firstName()    != null) employee.setFirstName(request.firstName());
        if (request.lastName()     != null) employee.setLastName(request.lastName());
        if (request.email()       != null) employee.setEmail(request.email().toLowerCase());
        if (request.phone()        != null) employee.setPhone(request.phone());
        if (request.departmentId() != null) {
            employee.setDepartmentId(request.departmentId());
            if (request.departmentName() != null) employee.setDepartmentName(request.departmentName());
        }
        if (request.positionId() != null) {
            employee.setPositionId(request.positionId());
            if (request.positionTitle() != null) employee.setPositionTitle(request.positionTitle());
        }
        if (request.baseSalary() != null) {
            String currency = request.currency() != null ? request.currency() : 
                (employee.getSalary() != null ? employee.getSalary().getCurrency() : "XOF");
            employee.setSalary(Salary.of(request.baseSalary(), currency));
        }
        if (request.contractType() != null) {
            employee.setContract(Contract.of(
                    request.contractType(),
                    employee.getContract() != null ? employee.getContract().getStartDate() : null,
                    request.contractEndDate()));
        }

        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Auditable(action = "TERMINATE_EMPLOYEE", entity = "Employee")
    @PreAuthorize("hasPermission(null, 'employee:update')")
    public EmployeeResponse terminate(UUID id, LocalDate terminationDate) {        Employee employee = findOrThrow(id);
        employee.terminate(terminationDate);
        employee = employeeRepository.save(employee);

        eventPublisher.publish(new EmployeeTerminatedEvent(
                employee.getId(), employee.getEmployeeNumber(), terminationDate));

        return employeeMapper.toResponse(employee);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'employee:read')")
    public EmployeeResponse findById(UUID id) {
        return employeeMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'employee:read')")
    public PageResponse<EmployeeResponse> findAll(String name, String status,
                                                    UUID departmentId, Pageable pageable) {
        Specification<Employee> spec = EmployeeSpecification.build(name, status, departmentId);
        return PageResponse.from(
                employeeRepository.findAll(spec, pageable).map(employeeMapper::toResponse));
    }

    @Auditable(action = "UPDATE_EMPLOYEE_PHOTO", entity = "Employee")
    @PreAuthorize("hasPermission(null, 'employee:update')")
    public EmployeeResponse updatePhoto(UUID id, String photoUrl) {
        Employee employee = findOrThrow(id);
        employee.setPhotoUrl(photoUrl);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Employee findOrThrow(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND, id));
    }

    private String generateEmployeeNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        String random = String.format("%05d", (int) (Math.random() * 99999));
        return "EMP-" + year + "-" + random;
    }
}
