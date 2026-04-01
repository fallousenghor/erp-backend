import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ContributionsService } from '../contributions.service';
import { ApiService } from '../../../core/services/api.service';
import { ToastrService } from 'ngx-toastr';

// Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-contribution-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatProgressSpinnerModule,
    MatCardModule
  ],
  templateUrl: './contribution-form.component.html',
  styleUrls: ['./contribution-form.component.scss']
})
export class ContributionFormComponent implements OnInit {
  contributionForm: FormGroup;
  isEditMode = false;
  contributionId: number | null = null;
  loading = false;
  members: any[] = [];
  ceremonialYears: any[] = [];

  statuses = [
    { value: 'PAYE', label: 'Payée' },
    { value: 'IMPAYE', label: 'Impayée' },
    { value: 'PARTIEL', label: 'Paiement partiel' },
    { value: 'EXONERE', label: 'Exonérée' }
  ];

  constructor(
    private fb: FormBuilder,
    private contributionsService: ContributionsService,
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.contributionForm = this.fb.group({
      memberId: ['', Validators.required],
      ceremonialYearId: ['', Validators.required],
      expectedAmount: ['', [Validators.required, Validators.min(0)]],
      paidAmount: [0, [Validators.required, Validators.min(0)]],
      dueDate: ['', Validators.required],
      paymentDate: [''],
      status: ['IMPAYE', Validators.required],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadMembers();
    this.loadCeremonialYears();

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.contributionId = +params['id'];
        this.loadContribution();
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

  loadCeremonialYears(): void {
    this.api.get('/ceremonial-years').subscribe({
      next: (response: any) => {
        this.ceremonialYears = response.data;
        const activeYear = this.ceremonialYears.find(y => y.active);
        if (activeYear && !this.isEditMode) {
          this.contributionForm.patchValue({ ceremonialYearId: activeYear.id });
        }
      }
    });
  }

  loadContribution(): void {
    if (!this.contributionId) return;

    this.loading = true;
    this.contributionsService.getContributions().subscribe({
      next: (response) => {
        const contribution = response.data.find((c: any) => c.id === this.contributionId);
        if (contribution) {
          this.contributionForm.patchValue({
            memberId: contribution.memberId,
            ceremonialYearId: contribution.ceremonialYearId,
            expectedAmount: contribution.expectedAmount,
            paidAmount: contribution.paidAmount,
            dueDate: contribution.dueDate ? new Date(contribution.dueDate) : '',
            paymentDate: contribution.paymentDate ? new Date(contribution.paymentDate) : '',
            status: contribution.status,
            notes: contribution.notes
          });
        }
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement');
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.contributionForm.invalid) {
      this.markFormGroupTouched(this.contributionForm);
      return;
    }

    this.loading = true;
    const formData = this.contributionForm.value;

    const request = this.isEditMode && this.contributionId
      ? this.contributionsService.updateContribution(this.contributionId, formData)
      : this.contributionsService.createContribution(formData);

    request.subscribe({
      next: () => {
        this.toastr.success(
          this.isEditMode ? 'Cotisation modifiée' : 'Cotisation créée'
        );
        this.router.navigate(['/contributions']);
      },
      error: () => {
        this.toastr.error('Erreur lors de l\'enregistrement');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/contributions']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get f() { return this.contributionForm.controls; }
}