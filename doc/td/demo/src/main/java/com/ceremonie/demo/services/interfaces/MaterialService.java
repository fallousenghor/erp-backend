package com.ceremonie.demo.services.interfaces;



import java.util.List;

import com.ceremonie.demo.dto.request.CreateMaterialRequest;
import com.ceremonie.demo.dto.response.MaterialResponse;

public interface MaterialService {
    MaterialResponse createMaterial(CreateMaterialRequest request);
    MaterialResponse updateMaterial(Long id, CreateMaterialRequest request);
    MaterialResponse getMaterialById(Long id);
    List<MaterialResponse> getAllMaterials();
    List<MaterialResponse> searchMaterials(String search);
    List<MaterialResponse> getLowStockMaterials(Integer threshold);
    void deleteMaterial(Long id);
}