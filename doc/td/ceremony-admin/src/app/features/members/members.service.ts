import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface Member {
  id: number;
  memberNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  active: boolean;
  registrationDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class MembersService {
  constructor(private api: ApiService) {}

  getMembers(): Observable<any> {
    return this.api.get<Member[]>('/members');
  }

  getMemberById(id: number): Observable<any> {
    return this.api.get<Member>(`/members/${id}`);
  }

  createMember(member: any): Observable<any> {
    return this.api.post('/members', member);
  }

  updateMember(id: number, member: any): Observable<any> {
    return this.api.put(`/members/${id}`, member);
  }

  deleteMember(id: number): Observable<any> {
    return this.api.delete(`/members/${id}`);
  }

  searchMembers(query: string): Observable<any> {
    return this.api.get<Member[]>(`/members/search?query=${query}`);
  }
}
