import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface Contribution {
  id: number;
  memberId: number;
  memberName: string;
  memberNumber: string;
  ceremonialYearId: number;
  ceremonialYear: number;
  expectedAmount: number;
  paidAmount: number;
  remainingAmount: number;
  status: 'PAYE' | 'IMPAYE' | 'PARTIEL' | 'EXONERE';
  dueDate: string;
  paymentDate: string;
  notes: string;
}

@Injectable({
  providedIn: 'root'
})
export class ContributionsService {
  constructor(private api: ApiService) {}

  getContributions(): Observable<any> {
    return this.api.get<Contribution[]>('/contributions');
  }

  getContributionsByMember(memberId: number): Observable<any> {
    return this.api.get<Contribution[]>(`/contributions/member/${memberId}`);
  }

  getContributionsByYear(yearId: number): Observable<any> {
    return this.api.get<Contribution[]>(`/contributions/year/${yearId}`);
  }

  getUnpaidContributions(): Observable<any> {
    return this.api.get<Contribution[]>('/contributions/unpaid');
  }

  createContribution(contribution: any): Observable<any> {
    return this.api.post('/contributions', contribution);
  }

  updateContribution(id: number, contribution: any): Observable<any> {
    return this.api.put(`/contributions/${id}`, contribution);
  }

  markAsPaid(contributionId: number, transactionId: number): Observable<any> {
    return this.api.put(`/contributions/${contributionId}/mark-paid/${transactionId}`, {});
  }

  deleteContribution(id: number): Observable<any> {
    return this.api.delete(`/contributions/${id}`);
  }
}