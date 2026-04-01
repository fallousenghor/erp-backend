export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  role: 'ADMIN' | 'TRESORIER' | 'SECRETAIRE' | 'MEMBRE_BUREAU' | 'MEMBRE';
  active: boolean;
  lastLogin?: string;
  createdAt?: string;
}

// src/app/core/models/member.model.ts
export interface Member {
  id: number;
  memberNumber: string;
  firstName: string;
  lastName: string;
  email?: string;
  phoneNumber: string;
  secondaryPhone?: string;
  dateOfBirth?: string;
  address?: string;
  registrationDate: string;
  photoUrl?: string;
  active: boolean;
  emergencyContact?: string;
  emergencyPhone?: string;
  notes?: string;
  badge?: Badge;
}

export interface Badge {
  id: number;
  badgeNumber: string;
  issueDate: string;
  expiryDate?: string;
  pdfUrl?: string;
  active: boolean;
}

// src/app/core/models/ceremonial-year.model.ts
export interface CeremonialYear {
  id: number;
  year: number;
  theme: string;
  description?: string;
  startDate: string;
  endDate: string;
  active: boolean;
  initialBudget?: number;
  totalIncome: number;
  totalExpense: number;
  totalContributions: number;
  balance: number;
  mediaCount?: number;
  eventCount?: number;
  transactionCount?: number;
}
