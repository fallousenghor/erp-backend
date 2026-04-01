import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  role: string;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
  updatedAt: string;
}

export interface CreateUserRequest {
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  password: string;
  role: string;
}

export interface UpdateUserRequest {
  username?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  role?: string;
  status?: 'ACTIVE' | 'INACTIVE';
}

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  constructor(private api: ApiService) {}

  getUsers(): Observable<any> {
    return this.api.get<User[]>('/users');
  }

  getUserById(id: number): Observable<any> {
    return this.api.get<User>(`/users/${id}`);
  }

  createUser(user: CreateUserRequest): Observable<any> {
    return this.api.post('/users', user);
  }

  updateUser(id: number, user: UpdateUserRequest): Observable<any> {
    return this.api.put(`/users/${id}`, user);
  }

  deleteUser(id: number): Observable<any> {
    return this.api.delete(`/users/${id}`);
  }

  changePassword(id: number, newPassword: string): Observable<any> {
    return this.api.put(`/users/${id}/password`, { password: newPassword });
  }
}