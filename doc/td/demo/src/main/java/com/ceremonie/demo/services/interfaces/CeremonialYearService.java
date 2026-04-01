package com.ceremonie.demo.services.interfaces;


import java.util.List;

import com.ceremonie.demo.dto.request.CreateCeremonialYearRequest;
import com.ceremonie.demo.dto.response.CeremonialYearResponse;

public interface CeremonialYearService {
    CeremonialYearResponse createCeremonialYear(CreateCeremonialYearRequest request);
    CeremonialYearResponse updateCeremonialYear(Long id, CreateCeremonialYearRequest request);
    CeremonialYearResponse getCeremonialYearById(Long id);
    CeremonialYearResponse getActiveCeremonialYear();
    List<CeremonialYearResponse> getAllCeremonialYears();
    void activateCeremonialYear(Long id);
    void deleteCeremonialYear(Long id);
}
