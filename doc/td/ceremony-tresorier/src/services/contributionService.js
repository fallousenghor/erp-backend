// src/services/contributionService.js
import api from './api';

const API_URL = import.meta.env.VITE_API_URL;

export const contributionService = {
  getContributions: () => api.get(`${API_URL}/contributions`),
  
  getContributionsByMember: (memberId) => 
    api.get(`${API_URL}/contributions/member/${memberId}`),
  
  getUnpaidContributions: () => 
    api.get(`${API_URL}/contributions/unpaid`),
  
  createContribution: (contributionData) => 
    api.post(`${API_URL}/contributions`, contributionData),
  
  updateContribution: (id, contributionData) => 
    api.put(`${API_URL}/contributions/${id}`, contributionData),
  
  markAsPaid: (contributionId, transactionId) => 
    api.put(`${API_URL}/contributions/${contributionId}/mark-paid/${transactionId}`, {}),
};