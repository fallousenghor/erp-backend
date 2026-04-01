// src/services/authService.js
import api from './api';
import { jwtDecode } from 'jwt-decode';

const API_URL = import.meta.env.VITE_API_URL;

export const authService = {
  login: async (username, password) => {
    const response = await api.post(`${API_URL}/auth/login`, {
      username,
      password,
    });
    
    if (response.data.success) {
      const { token, ...userData } = response.data.data;
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));
      return userData;
    }
    throw new Error(response.data.message);
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  isAuthenticated: () => {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
      const decoded = jwtDecode(token);
      return decoded.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  },

  hasRole: (role) => {
    const user = authService.getCurrentUser();
    return user?.role === role;
  },
};