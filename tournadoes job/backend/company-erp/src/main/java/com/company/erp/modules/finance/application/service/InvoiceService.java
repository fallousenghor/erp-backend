package com.company.erp.modules.finance.application.service;

import com.company.erp.modules.finance.application.dto.request.CreateInvoiceRequest;
import com.company.erp.modules.finance.application.dto.request.ProcessPaymentRequest;
import com.company.erp.modules.finance.application.dto.request.UpdateInvoiceRequest;
import com.company.erp.modules.finance.application.dto.response.FinancialSummaryResponse;
import com.company.erp.modules.finance.application.dto.response.InvoiceResponse;
import com.company.erp.modules.finance.application.mapper.InvoiceMapper;
import com.company.erp.modules.finance.domain.event.InvoiceCancelledEvent;
import com.company.erp.modules.finance.domain.event.InvoiceCreatedEvent;
import com.company.erp.modules.finance.domain.event.InvoicePaidEvent;
import com.company.erp.modules.finance.domain.model.Expense;
import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.model.Payment;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceNumber;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import com.company.erp.modules.finance.domain.model.valueobject.Money;
import com.company.erp.modules.finance.domain.model.valueobject.TaxRate;
import com.company.erp.modules.finance.domain.repository.ExpenseRepository;
import com.company.erp.modules.finance.domain.repository.InvoiceRepository;
import com.company.erp.modules.finance.infrastructure.persistence.specification.InvoiceSpecification;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ExpenseRepository expenseRepository;
    private final InvoiceMapper invoiceMapper;
    private final DomainEventPublisher eventPublisher;

    // ── Commands ─────────────────────────────────────────────────────────────

    @Auditable(action = "CREATE_INVOICE", entity = "Invoice")
    @PreAuthorize("hasPermission(null, 'invoice:create')")
    public InvoiceResponse create(CreateInvoiceRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        Invoice invoice = Invoice.builder()
                .invoiceNumber(InvoiceNumber.generate().value())
                .clientName(request.clientName())
                .clientEmail(request.clientEmail())
                .clientAddress(request.clientAddress())
                .issuedByName(currentUser)
                .issueDate(request.issueDate())
                .dueDate(request.dueDate())
                .currency(request.currency())
                .notes(request.notes())
                .build();

        if (request.taxRate() != null) {
            invoice.setTaxRate(new TaxRate(request.taxRate()));
        }

        request.items().forEach(item ->
                invoice.addItem(item.description(), item.quantity(), item.unitPrice()));

        Invoice saved = invoiceRepository.save(invoice);
        eventPublisher.publish(new InvoiceCreatedEvent(
                saved.getId(), saved.getInvoiceNumber(), saved.getTotal()));

        log.info("Invoice created: {}", saved.getInvoiceNumber());
        return invoiceMapper.toResponse(saved);
    }

    @Auditable(action = "UPDATE_INVOICE", entity = "Invoice")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public InvoiceResponse update(UUID id, UpdateInvoiceRequest request) {
        Invoice invoice = findOrThrow(id);
        
        if (request.clientName() != null) {
            invoice.setClientName(request.clientName());
        }
        if (request.clientEmail() != null) {
            invoice.setClientEmail(request.clientEmail());
        }
        if (request.clientAddress() != null) {
            invoice.setClientAddress(request.clientAddress());
        }
        if (request.issueDate() != null) {
            invoice.setIssueDate(request.issueDate());
        }
        if (request.dueDate() != null) {
            invoice.setDueDate(request.dueDate());
        }
        if (request.currency() != null) {
            invoice.setCurrency(request.currency());
        }
        if (request.notes() != null) {
            invoice.setNotes(request.notes());
        }
        if (request.taxRate() != null) {
            invoice.setTaxRate(new TaxRate(request.taxRate()));
        }
        
        // Update items if provided
        if (request.items() != null && !request.items().isEmpty()) {
            invoice.getItems().clear();
            request.items().forEach(item ->
                    invoice.addItem(item.description(), item.quantity(), item.unitPrice()));
        }
        
        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice updated: {}", saved.getInvoiceNumber());
        return invoiceMapper.toResponse(saved);
    }

    @Auditable(action = "DELETE_INVOICE", entity = "Invoice")
    @PreAuthorize("hasPermission(null, 'invoice:delete')")
    public void delete(UUID id) {
        Invoice invoice = findOrThrow(id);
        invoiceRepository.delete(invoice);
        log.info("Invoice deleted: {}", invoice.getInvoiceNumber());
    }

    @Auditable(action = "SEND_INVOICE", entity = "Invoice")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public InvoiceResponse send(UUID id) {
        Invoice invoice = findOrThrow(id);
        invoice.send();
        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Auditable(action = "PROCESS_PAYMENT", entity = "Invoice")
    @PreAuthorize("hasPermission(null, 'payment:process')")
    public InvoiceResponse processPayment(UUID invoiceId, ProcessPaymentRequest request) {
        Invoice invoice = findOrThrow(invoiceId);

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(Money.of(request.amount(), invoice.getCurrency()))
                .paymentMethod(request.paymentMethod())
                .paymentDate(request.paymentDate())
                .reference(request.reference())
                .notes(request.notes())
                .build();

        invoice.addPayment(payment);
        invoice.markPaid();
        Invoice saved = invoiceRepository.save(invoice);

        eventPublisher.publish(new InvoicePaidEvent(
                saved.getId(), saved.getInvoiceNumber(), saved.getTotal()));

        return invoiceMapper.toResponse(saved);
    }

    @Auditable(action = "CANCEL_INVOICE", entity = "Invoice")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public InvoiceResponse cancel(UUID id) {
        Invoice invoice = findOrThrow(id);
        invoice.cancel();
        Invoice saved = invoiceRepository.save(invoice);
        eventPublisher.publish(new InvoiceCancelledEvent(saved.getId(), saved.getInvoiceNumber()));
        return invoiceMapper.toResponse(saved);
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public InvoiceResponse findById(UUID id) {
        return invoiceMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public PageResponse<InvoiceResponse> findAll(String clientName, String status, Pageable pageable) {
        return PageResponse.from(
                invoiceRepository.findAll(
                        InvoiceSpecification.build(clientName, status), pageable)
                        .map(invoiceMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public FinancialSummaryResponse getFinancialSummary() {
        BigDecimal totalRevenue = invoiceRepository.sumTotalByStatus(InvoiceStatus.PAID);
        BigDecimal totalPending = invoiceRepository.sumTotalByStatus(InvoiceStatus.SENT);
        BigDecimal totalExpenses = expenseRepository.sumAmountByStatus(Expense.ExpenseStatus.PAID);

        totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        totalPending = totalPending != null ? totalPending : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;

        return new FinancialSummaryResponse(
                totalRevenue,
                totalRevenue,
                totalPending,
                totalExpenses,
                totalRevenue.subtract(totalExpenses),
                invoiceRepository.countByStatus(InvoiceStatus.PAID)
                        + invoiceRepository.countByStatus(InvoiceStatus.SENT),
                invoiceRepository.countByStatus(InvoiceStatus.PAID),
                invoiceRepository.countByStatus(InvoiceStatus.SENT),
                0L,
                "XOF"
        );
    }

    private Invoice findOrThrow(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.INVOICE_NOT_FOUND, id));
    }
}
