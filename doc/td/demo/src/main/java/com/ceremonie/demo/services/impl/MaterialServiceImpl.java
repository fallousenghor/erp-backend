package com.ceremonie.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremonie.demo.dto.request.CreateMaterialRequest;
import com.ceremonie.demo.dto.response.MaterialResponse;
import com.ceremonie.demo.entity.Material;
import com.ceremonie.demo.enums.MaterialStatus;
import com.ceremonie.demo.exceptions.DuplicateResourceException;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.MaterialRepository;
import com.ceremonie.demo.services.interfaces.MaterialService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    @Transactional
    public MaterialResponse createMaterial(CreateMaterialRequest request) {
        if (request.getReferenceNumber() != null && 
            materialRepository.existsByReferenceNumber(request.getReferenceNumber())) {
            throw new DuplicateResourceException("Le numéro de référence existe déjà");
        }

        Material material = Material.builder()
                .name(request.getName())
                .description(request.getDescription())
                .referenceNumber(request.getReferenceNumber())
                .quantity(request.getQuantity())
                .availableQuantity(request.getQuantity())
                .status(MaterialStatus.BON_ETAT)
                .purchaseDate(request.getPurchaseDate())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();

        Material savedMaterial = materialRepository.save(material);
        return mapToResponse(savedMaterial);
    }

    @Override
    @Transactional
    public MaterialResponse updateMaterial(Long id, CreateMaterialRequest request) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matériel non trouvé"));

        material.setName(request.getName());
        material.setDescription(request.getDescription());
        material.setQuantity(request.getQuantity());
        material.setPurchaseDate(request.getPurchaseDate());
        material.setLocation(request.getLocation());
        material.setNotes(request.getNotes());

        Material updatedMaterial = materialRepository.save(material);
        return mapToResponse(updatedMaterial);
    }

    @Override
    public MaterialResponse getMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matériel non trouvé"));
        return mapToResponse(material);
    }

    @Override
    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> searchMaterials(String search) {
        return materialRepository.searchMaterials(search).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> getLowStockMaterials(Integer threshold) {
        return materialRepository.findLowStock(threshold).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matériel non trouvé"));
        material.setDeleted(true);
        materialRepository.save(material);
    }

    private MaterialResponse mapToResponse(Material material) {
        Integer borrowedQuantity = material.getQuantity() - material.getAvailableQuantity();
        
        return MaterialResponse.builder()
                .id(material.getId())
                .name(material.getName())
                .description(material.getDescription())
                .referenceNumber(material.getReferenceNumber())
                .quantity(material.getQuantity())
                .availableQuantity(material.getAvailableQuantity())
                .borrowedQuantity(borrowedQuantity)
                .status(material.getStatus())
                .purchaseDate(material.getPurchaseDate())
                .location(material.getLocation())
                .photoUrl(material.getPhotoUrl())
                .build();
    }
}
