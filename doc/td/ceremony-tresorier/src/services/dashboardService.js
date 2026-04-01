// src/services/dashboardService.js
import api from './api';

const API_URL = import.meta.env.VITE_API_URL;

export const dashboardService = {
  getStats: () => api.get(`${API_URL}/dashboard/stats`),

  getChartData: () => api.get(`${API_URL}/dashboard/chart-data`),
};