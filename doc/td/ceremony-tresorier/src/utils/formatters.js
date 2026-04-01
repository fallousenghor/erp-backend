// src/utils/formatters.js

export const formatCurrency = (value) => {
  const numValue = Number(value);
  if (isNaN(numValue) || value === null || value === undefined) {
    return '0 F CFA';
  }
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF',
  }).format(numValue);
};

export const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('fr-FR');
};

export const formatNumber = (value) => {
  return new Intl.NumberFormat('fr-FR').format(value);
};