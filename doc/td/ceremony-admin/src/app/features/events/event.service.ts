import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface Event {
  id: number;
  ceremonialYearId: number;
  ceremonialYear: number;
  title: string;
  description: string;
  type: 'REUNION' | 'DEPLACEMENT' | 'CEREMONIE' | 'FORMATION' | 'AUTRE';
  startDate: string;
  endDate: string;
  location: string;
  address: string;
  participants: any[];
  organizerName: string;
  reminderSent: boolean;
  reminderDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class EventsService {
  constructor(private api: ApiService) {}

  getEvents(): Observable<any> {
    return this.api.get<Event[]>('/events/active-year');
  }

  getUpcomingEvents(): Observable<any> {
    return this.api.get<Event[]>('/events/upcoming');
  }

  getEventById(id: number): Observable<any> {
    return this.api.get<Event>(`/events/${id}`);
  }

  getEventsByMember(memberId: number): Observable<any> {
    return this.api.get<Event[]>(`/events/member/${memberId}`);
  }

  createEvent(event: any): Observable<any> {
    return this.api.post('/events', event);
  }

  updateEvent(id: number, event: any): Observable<any> {
    return this.api.put(`/events/${id}`, event);
  }

  deleteEvent(id: number): Observable<any> {
    return this.api.delete(`/events/${id}`);
  }

  addParticipant(eventId: number, memberId: number): Observable<any> {
    return this.api.post(`/events/${eventId}/participants/${memberId}`, {});
  }

  removeParticipant(eventId: number, memberId: number): Observable<any> {
    return this.api.delete(`/events/${eventId}/participants/${memberId}`);
  }
}