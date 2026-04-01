// src/services/memberService.js
import api from './api';

const API_URL = import.meta.env.VITE_API_URL;

export const memberService = {
  getMembers: () => api.get(`${API_URL}/members`),
  
  getMemberById: (id) => api.get(`${API_URL}/members/${id}`),
  
  createMember: (memberData) => api.post(`${API_URL}/members`, memberData),
  
  updateMember: (id, memberData) => api.put(`${API_URL}/members/${id}`, memberData),
  
  deleteMember: (id) => api.delete(`${API_URL}/members/${id}`),
  
  searchMembers: (query) => api.get(`${API_URL}/members/search?query=${query}`),
  
  getActiveMembers: () => api.get(`${API_URL}/members/active`),
};