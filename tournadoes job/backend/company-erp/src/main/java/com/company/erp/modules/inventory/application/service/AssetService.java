package com.company.erp.modules.inventory.application.service;

import com.company.erp.modules.inventory.application.dto.request.AssignAssetRequest;
import com.company.erp.modules.inventory.application.dto.request.CreateAssetRequest;
import com.company.erp.modules.inventory.application.dto.request.CreateAssetRequestWithMedia;
import com.company.erp.modules.inventory.application.dto.response.AssetResponse;
import com.company.erp.modules.inventory.application.mapper.AssetMapper;
import com.company.erp.modules.inventory.domain.event.AssetAssignedEvent;
import com.company.erp.modules.inventory.domain.event.AssetCreatedEvent;
import com.company.erp.modules.inventory.domain.event.AssetReturnedEvent;
import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetCode;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetCondition;
import com.company.erp.modules.inventory.domain.repository.AssetRepository;
import com.company.erp.modules.inventory.infrastructure.persistence.specification.AssetSpecification;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final DomainEventPublisher eventPublisher;

    @Auditable(action = "CREATE_ASSET", entity = "Asset")
    @PreAuthorize("hasPermission(null, 'asset:create')")
    public AssetResponse create(CreateAssetRequest request) {
        String code = AssetCode.generate(request.category().name()).value();

        if (assetRepository.existsByAssetCode(code)) {
            code = AssetCode.generate(request.category().name()).value();
        }

        Asset asset = Asset.builder()
                .assetCode(code)
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .conditionState(request.conditionState() != null
                        ? request.conditionState() : AssetCondition.NEW)
                .purchaseDate(request.purchaseDate())
                .purchasePrice(request.purchasePrice())
                .serialNumber(request.serialNumber())
                .brand(request.brand())
                .model(request.model())
                .location(request.location())
                .departmentId(request.departmentId())
                .build();

        asset = assetRepository.save(asset);
        eventPublisher.publish(new AssetCreatedEvent(asset.getId(), asset.getAssetCode(), asset.getName()));

        log.info("Asset created: [{}] {}", asset.getAssetCode(), asset.getName());
        return assetMapper.toResponse(asset);
    }

    @Auditable(action = "CREATE_ASSET", entity = "Asset")
    @PreAuthorize("hasPermission(null, 'asset:create')")
    public AssetResponse createWithMedia(CreateAssetRequestWithMedia request, 
                                          String imageUrl, String documentUrl) {
        String code = AssetCode.generate(request.category().name()).value();

        if (assetRepository.existsByAssetCode(code)) {
            code = AssetCode.generate(request.category().name()).value();
        }

        Asset asset = Asset.builder()
                .assetCode(code)
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .conditionState(request.conditionState() != null
                        ? request.conditionState() : AssetCondition.NEW)
                .purchaseDate(request.purchaseDate())
                .purchasePrice(request.purchasePrice())
                .serialNumber(request.serialNumber())
                .brand(request.brand())
                .model(request.model())
                .location(request.location())
                .departmentId(request.departmentId())
                .imageUrl(imageUrl)
                .documentUrl(documentUrl)
                .build();

        asset = assetRepository.save(asset);
        eventPublisher.publish(new AssetCreatedEvent(asset.getId(), asset.getAssetCode(), asset.getName()));

        log.info("Asset created with media: [{}] {}", asset.getAssetCode(), asset.getName());
        return assetMapper.toResponse(asset);
    }

    @Auditable(action = "ASSIGN_ASSET", entity = "Asset")
    @PreAuthorize("hasPermission(null, 'asset:assign')")
    public AssetResponse assign(UUID assetId, AssignAssetRequest request) {
        Asset asset = findOrThrow(assetId);
        asset.assignTo(request.employeeId(), request.employeeName(),
                request.assignedDate(), request.notes());
        asset = assetRepository.save(asset);

        eventPublisher.publish(new AssetAssignedEvent(
                asset.getId(), asset.getAssetCode(),
                request.employeeId(), request.employeeName()));

        return assetMapper.toResponse(asset);
    }

    @Auditable(action = "RETURN_ASSET", entity = "Asset")
    @PreAuthorize("hasPermission(null, 'asset:assign')")
    public AssetResponse returnAsset(UUID assetId) {
        Asset asset = findOrThrow(assetId);
        asset.returnAsset(LocalDate.now());
        asset = assetRepository.save(asset);

        eventPublisher.publish(new AssetReturnedEvent(asset.getId(), asset.getAssetCode()));
        return assetMapper.toResponse(asset);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'asset:read')")
    public AssetResponse findById(UUID id) {
        return assetMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'asset:read')")
    public PageResponse<AssetResponse> findAll(String name, String status,
                                                String category, Pageable pageable) {
        return PageResponse.from(
                assetRepository.findAll(
                        AssetSpecification.build(name, status, category), pageable)
                        .map(assetMapper::toResponse));
    }

    private Asset findOrThrow(UUID id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ASSET_NOT_FOUND, id));
    }

    @Auditable(action = "UPDATE_ASSET", entity = "Asset")
    @PreAuthorize("hasPermission(null, 'asset:create')")
    public AssetResponse update(UUID id, CreateAssetRequest request) {
        Asset asset = findOrThrow(id);

        if (request.name()        != null) asset.setName(request.name());
        if (request.description() != null) asset.setDescription(request.description());
        if (request.location()    != null) asset.setLocation(request.location());
        if (request.brand()       != null) asset.setBrand(request.brand());
        if (request.model()       != null) asset.setModel(request.model());
        if (request.conditionState() != null) asset.setConditionState(request.conditionState());
        if (request.departmentId()   != null) asset.setDepartmentId(request.departmentId());

        return assetMapper.toResponse(assetRepository.save(asset));
    }

    @Auditable(action = "UPDATE_ASSET_MEDIA", entity = "Asset")
    @PreAuthorize("hasPermission(null, 'asset:create')")
    public AssetResponse updateMedia(UUID id, String imageUrl, String documentUrl) {
        Asset asset = findOrThrow(id);
        if (imageUrl != null) {
            asset.setImageUrl(imageUrl);
        }
        if (documentUrl != null) {
            asset.setDocumentUrl(documentUrl);
        }
        return assetMapper.toResponse(assetRepository.save(asset));
    }
}
