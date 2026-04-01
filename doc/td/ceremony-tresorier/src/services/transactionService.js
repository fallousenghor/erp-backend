// src/services/transactionService.js
import api from './api';

const API_URL = import.meta.env.VITE_API_URL;

export const transactionService = {
  getTransactions: () => api.get(`${API_URL}/transactions/active-year`),
  
  getTransactionById: (id) => api.get(`${API_URL}/transactions/${id}`),
  
  createTransaction: (transactionData) => 
    api.post(`${API_URL}/transactions`, transactionData),
  
  deleteTransaction: (id) => api.delete(`${API_URL}/transactions/${id}`),
  
  getFinancialReport: (yearId) => 
    api.get(`${API_URL}/transactions/report/${yearId}`),
  
  getTransactionsByType: (type) => 
    api.get(`${API_URL}/transactions/type/${type}`),
};