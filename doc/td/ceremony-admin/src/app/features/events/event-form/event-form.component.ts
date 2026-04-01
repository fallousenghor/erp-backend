import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../core/services/api.service';
import { ToastrService } from 'ngx-toastr';
import { EventsService } from '../event.service';

// Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatCardModule,
    MatChipsModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss']
})
export class EventFormComponent implements OnInit {
  eventForm: FormGroup;
  isEditMode = false;
  eventId: number | null = null;
  loading = false;
  ceremonialYears: any[] = [];
  members: any[] = [];
  selectedParticipants: any[] = [];

  eventTypes = [
    { value: 'REUNION', label: 'Réunion' },
    { value: 'DEPLACEMENT', label: 'Déplacement' },
    { value: 'CEREMONIE', label: 'Cérémonie' },
    { value: 'FORMATION', label: 'Formation' },
    { value: 'AUTRE', label: 'Autre' }
  ];

  constructor(
    private fb: FormBuilder,
    private eventsService: EventsService,
    private api: ApiService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
    this.eventForm = this.fb.group({
      ceremonialYearId: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      type: ['REUNION', Validators.required],
      startDate: ['', Validators.required],
      endDate: [''],
      location: [''],
      address: [''],
      reminderDate: [''],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadCeremonialYears();
    this.loadMembers();

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.eventId = +params['id'];
        this.loadEvent();
      }
    });
  }

  loadCeremonialYears(): void {
    this.api.get('/ceremonial-years').subscribe({
      next: (response: any) => {
        this.ceremonialYears = response.data;
        const activeYear = this.ceremonialYears.find(y => y.active);
        if (activeYear && !this.isEditMode) {
          this.eventForm.patchValue({ ceremonialYearId: activeYear.id });
        }
      }
    });
  }

  loadMembers(): void {
    this.api.get('/members/active').subscribe({
      next: (response: any) => {
        this.members = response.data;
      }
    });
  }

  loadEvent(): void {
    if (!this.eventId) return;

    this.loading = true;
    this.eventsService.getEventById(this.eventId).subscribe({
      next: (response) => {
        this.eventForm.patchValue(response.data);
        this.selectedParticipants = response.data.participants || [];
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement');
        this.loading = false;
      }
    });
  }

  toggleParticipant(member: any): void {
    const index = this.selectedParticipants.findIndex(p => p.id === member.id);
    if (index > -1) {
      this.selectedParticipants.splice(index, 1);
    } else {
      this.selectedParticipants.push(member);
    }
  }

  isParticipantSelected(member: any): boolean {
    return this.selectedParticipants.some(p => p.id === member.id);
  }

  onSubmit(): void {
    if (this.eventForm.invalid) {
      this.markFormGroupTouched(this.eventForm);
      return;
    }

    this.loading = true;
    const formData = {
      ...this.eventForm.value,
      participantIds: this.selectedParticipants.map(p => p.id)
    };

    const request = this.isEditMode && this.eventId
      ? this.eventsService.updateEvent(this.eventId, formData)
      : this.eventsService.createEvent(formData);

    request.subscribe({
      next: () => {
        this.toastr.success(
          this.isEditMode ? 'Événement modifié' : 'Événement créé'
        );
        this.router.navigate(['/events']);
      },
      error: () => {
        this.toastr.error('Erreur lors de l\'enregistrement');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/events']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get f() { return this.eventForm.controls; }
}