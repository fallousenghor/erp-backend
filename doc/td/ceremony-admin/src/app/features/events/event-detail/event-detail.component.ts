import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { EventsService } from '../event.service';

// Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';
import { CurrencyXofPipe } from '../../../shared/pipes/currency-xof.pipe';

@Component({
  selector: 'app-event-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatGridListModule,
    MatTooltipModule,
    LoadingSpinnerComponent,
    CurrencyXofPipe
  ],
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.scss']
})
export class EventDetailComponent implements OnInit {
  event: any = null;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventsService: EventsService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const eventId = this.route.snapshot.params['id'];
    this.loadEvent(eventId);
  }

  loadEvent(id: number): void {
    this.loading = true;
    this.eventsService.getEventById(id).subscribe({
      next: (response) => {
        this.event = response.data;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement');
        this.loading = false;
      }
    });
  }

  editEvent(): void {
    this.router.navigate(['/events/edit', this.event.id]);
  }

  goBack(): void {
    this.router.navigate(['/events']);
  }

  deleteEvent(): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet événement ?')) {
      this.eventsService.deleteEvent(this.event.id).subscribe({
        next: () => {
          this.toastr.success('Événement supprimé');
          this.router.navigate(['/events']);
        },
        error: () => this.toastr.error('Erreur lors de la suppression')
      });
    }
  }


  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}