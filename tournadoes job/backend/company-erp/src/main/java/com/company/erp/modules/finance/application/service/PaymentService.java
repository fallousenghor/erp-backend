package com.company.erp.modules.finance.application.service;

import com.company.erp.modules.finance.application.dto.response.PaymentResponse;
import com.company.erp.modules.finance.domain.model.Payment;
import com.company.erp.modules.finance.domain.repository.PaymentRepository;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public List<PaymentResponse> findByInvoice(UUID invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public PaymentResponse findById(UUID id) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PAYMENT_NOT_FOUND, id));
        return toResponse(p);
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getInvoice().getId(),
                p.getInvoice().getInvoiceNumber(),
                p.getAmount().getAmount(),
                p.getAmount().getCurrency(),
                p.getPaymentMethod(),
                p.getPaymentDate(),
                p.getReference()
        );
    }
}
