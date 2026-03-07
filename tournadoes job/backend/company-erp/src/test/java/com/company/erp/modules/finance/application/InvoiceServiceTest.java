package com.company.erp.modules.finance.application;

import com.company.erp.modules.finance.application.dto.request.CreateInvoiceRequest;
import com.company.erp.modules.finance.application.dto.response.InvoiceResponse;
import com.company.erp.modules.finance.application.mapper.InvoiceMapper;
import com.company.erp.modules.finance.application.service.InvoiceService;
import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.repository.InvoiceRepository;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceMapper invoiceMapper;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks private InvoiceService invoiceService;

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> invoiceService.findById(id));
    }

    @Test
    void create_generatesInvoiceNumberAndPublishesEvent() {
        CreateInvoiceRequest request = new CreateInvoiceRequest(
                "ACME Corp", "acme@corp.com", "123 Main St",
                LocalDate.now(), LocalDate.now().plusDays(30),
                "XOF", BigDecimal.ZERO, null,
                List.of());

        Invoice saved = mock(Invoice.class);
        when(saved.getId()).thenReturn(UUID.randomUUID());
        when(saved.getInvoiceNumber()).thenReturn("INV-2025-000001");

        when(invoiceRepository.save(any())).thenReturn(saved);
        when(invoiceMapper.toResponse(any())).thenReturn(mock(InvoiceResponse.class));

        assertDoesNotThrow(() -> invoiceService.create(request));
        verify(eventPublisher).publish(any());
    }
}
