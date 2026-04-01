package com.ceremonie.demo.services.interfaces;


import java.util.List;

import com.ceremonie.demo.dto.request.CreateContributionRequest;
import com.ceremonie.demo.dto.response.ContributionResponse;

public interface ContributionService {
    ContributionResponse createContribution(CreateContributionRequest request);
    ContributionResponse updateContribution(Long id, CreateContributionRequest request);
    ContributionResponse getContributionById(Long id);
    List<ContributionResponse> getAllContributions();
    List<ContributionResponse> getContributionsByMember(Long memberId);
    List<ContributionResponse> getContributionsByYear(Long yearId);
    List<ContributionResponse> getUnpaidContributions();
    void markAsPaid(Long contributionId, Long transactionId);
    void deleteContribution(Long id);
}
