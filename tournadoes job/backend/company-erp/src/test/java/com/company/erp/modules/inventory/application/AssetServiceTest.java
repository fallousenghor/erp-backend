package com.company.erp.modules.inventory.application;

import com.company.erp.modules.inventory.application.dto.request.AssignAssetRequest;
import com.company.erp.modules.inventory.application.mapper.AssetMapper;
import com.company.erp.modules.inventory.application.service.AssetService;
import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import com.company.erp.modules.inventory.domain.repository.AssetRepository;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock private AssetRepository assetRepository;
    @Mock private AssetMapper assetMapper;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks private AssetService assetService;

    @Test
    void findById_whenNotFound_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(assetRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> assetService.findById(id));
    }

    @Test
    void assign_whenAssetNotAvailable_throwsBusinessException() {
        UUID assetId = UUID.randomUUID();
        Asset asset = mock(Asset.class);
        when(asset.getStatus()).thenReturn(AssetStatus.ASSIGNED);

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        doThrow(new BusinessException(com.company.erp.shared.exception.ErrorCode.ASSET_ALREADY_ASSIGNED, "Asset is already assigned"))
                .when(asset).assignTo(any(), any(), any(), any());

        AssignAssetRequest request = new AssignAssetRequest(
                UUID.randomUUID(), "John Doe", LocalDate.now(), null);

        assertThrows(BusinessException.class, () -> assetService.assign(assetId, request));
    }
}
