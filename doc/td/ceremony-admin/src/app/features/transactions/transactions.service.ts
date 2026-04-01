
import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface Transaction {
  id: number;
  ceremonialYearId: number;
  ceremonialYear: number;
  transactionDate: string;
  type: 'ENTREE' | 'SORTIE' | 'COTISATION' | 'DON';
  amount: number;
  description: string;
  category: string;
  paymentMethod: string;
  referenceNumber: string;
  memberName?: string;
  recordedByName?: string;
}

export interface FinancialReport {
  ceremonialYearId: number;
  year: number;
  totalIncome: number;
  totalExpense: number;
  totalContributions: number;
  balance: number;
  initialBudget: number;
  transactionCount: number;
  contributionCount: number;
  paidContributionCount: number;
  unpaidContributionCount: number;
  expensesByCategory: { [key: string]: number };
  recentTransactions: Transaction[];
}

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {
  constructor(private api: ApiService) {}

  getTransactions(): Observable<any> {
    return this.api.get<Transaction[]>('/transactions/active-year');
  }

  getTransactionById(id: number): Observable<any> {
    return this.api.get<Transaction>(`/transactions/${id}`);
  }

  getTransactionsByYear(yearId: number): Observable<any> {
    return this.api.get<Transaction[]>(`/transactions/year/${yearId}`);
  }

  getTransactionsByType(type: string): Observable<any> {
    return this.api.get<Transaction[]>(`/transactions/type/${type}`);
  }

  getFinancialReport(yearId: number): Observable<any> {
    return this.api.get<FinancialReport>(`/transactions/report/${yearId}`);
  }

  createTransaction(transaction: any): Observable<any> {
    return this.api.post('/transactions', transaction);
  }

  deleteTransaction(id: number): Observable<any> {
    return this.api.delete(`/transactions/${id}`);
  }
}