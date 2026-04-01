import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface DashboardStats {
  totalMembers: number;
  activeMembers: number;
  totalUsers: number;
  currentBalance: number;
  totalIncomeThisYear: number;
  totalExpenseThisYear: number;
  upcomingEvents: number;
  unpaidContributions: number;
  activeMaterialLoans: number;
  totalMedias: number;
  activeCeremonialYear: string;
}

export interface ChartData {
  labels: string[];
  incomeData: number[];
  expenseData: number[];
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private api: ApiService) {}

  getStats(): Observable<any> {
    return this.api.get<DashboardStats>('/dashboard/stats');
  }

  getChartData(): Observable<any> {
    return this.api.get<ChartData>('/dashboard/chart-data');
  }
}