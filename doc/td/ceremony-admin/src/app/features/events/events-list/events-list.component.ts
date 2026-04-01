import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EventsService, Event } from '../event.service';
import { ToastrService } from 'ngx-toastr';

// FullCalendar
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';

// Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-events-list',
  standalone: true,
  imports: [
    CommonModule,
    FullCalendarModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatButtonToggleModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './events-list.component.html',
  styleUrls: ['./events-list.component.scss']
})
export class EventsListComponent implements OnInit {
  events: Event[] = [];
  upcomingEvents: Event[] = [];
  pastEvents: Event[] = [];
  loading = true;
  viewMode: 'list' | 'calendar' = 'list';
  selectedFilter = 'upcoming';

  eventTypes = [
    { value: 'REUNION', label: 'Réunion', icon: 'groups', color: '#2196f3' },
    { value: 'DEPLACEMENT', label: 'Déplacement', icon: 'flight', color: '#ff9800' },
    { value: 'CEREMONIE', label: 'Cérémonie', icon: 'celebration', color: '#9c27b0' },
    { value: 'FORMATION', label: 'Formation', icon: 'school', color: '#4caf50' },
    { value: 'AUTRE', label: 'Autre', icon: 'event', color: '#607d8b' }
  ];

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin],
    initialView: 'dayGridMonth',
    weekends: true,
    events: [],
    eventClick: this.handleEventClick.bind(this),
    eventDisplay: 'block',
    height: 'auto'
  };

  constructor(
    private eventsService: EventsService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.loading = true;
    this.eventsService.getEvents().subscribe({
      next: (response) => {
        this.events = response.data;
        this.categorizeEvents();
        this.updateCalendarEvents();
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur de chargement des événements');
        this.loading = false;
      }
    });
  }

  categorizeEvents(): void {
    const now = new Date();
    this.upcomingEvents = this.events.filter(e => new Date(e.startDate) > now);
    this.pastEvents = this.events.filter(e => new Date(e.startDate) <= now);
  }

  getDisplayedEvents(): Event[] {
    switch (this.selectedFilter) {
      case 'upcoming':
        return this.upcomingEvents;
      case 'past':
        return this.pastEvents;
      default:
        return this.events;
    }
  }

  getEventTypeConfig(type: string): any {
    return this.eventTypes.find(t => t.value === type) || this.eventTypes[4];
  }

  viewEvent(event: Event): void {
    this.router.navigate(['/events', event.id]);
  }

  editEvent(event: Event): void {
    this.router.navigate(['/events/edit', event.id]);
  }

  deleteEvent(event: Event): void {
    if (confirm(`Supprimer l'événement "${event.title}" ?`)) {
      this.eventsService.deleteEvent(event.id).subscribe({
        next: () => {
          this.toastr.success('Événement supprimé');
          this.loadEvents();
        },
        error: () => {
          this.toastr.error('Erreur lors de la suppression');
        }
      });
    }
  }

  addEvent(): void {
    this.router.navigate(['/events/new']);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric'
    });
  }

  formatTime(date: string): string {
    return new Date(date).toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getDaysUntil(date: string): number {
    const now = new Date();
    const eventDate = new Date(date);
    const diff = eventDate.getTime() - now.getTime();
    return Math.ceil(diff / (1000 * 60 * 60 * 24));
  }

  handleEventClick(info: any): void {
    const eventId = info.event.id;
    const event = this.events.find(e => e.id === eventId);
    if (event) {
      this.viewEvent(event);
    }
  }

  private updateCalendarEvents(): void {
    this.calendarOptions = {
      ...this.calendarOptions,
      events: this.events.map(event => ({
        id: event.id.toString(),
        title: event.title,
        start: event.startDate,
        backgroundColor: this.getEventTypeConfig(event.type).color,
        borderColor: this.getEventTypeConfig(event.type).color
      }))
    };
  }
}