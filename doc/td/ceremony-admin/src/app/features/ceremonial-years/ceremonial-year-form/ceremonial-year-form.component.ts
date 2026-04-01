import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../core/services/api.service';
import { ToastrService } from 'ngx-toastr';

// Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-ceremonial-year-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatProgressSpinnerModule,
    MatCardModule
  ],
  templateUrl: './ceremonial-year-form.component.html',
  styleUrls: ['./ceremonial-year-form.component.scss']
})
export class CeremonialYearFormComponent implements OnInit {
  yearForm: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.yearForm = this.fb.group({
      year: ['', [Validators.required, Validators.min(2000), Validators.max(2100)]],
      theme: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      initialBudget: [0, [Validators.required, Validators.min(0)]],
      description: ['']
    });
  }

  ngOnInit(): void {
    // No initialization needed for create-only form
  }

  onSubmit(): void {
    if (this.yearForm.invalid) {
      this.markFormGroupTouched(this.yearForm);
      return;
    }

    this.loading = true;
    const formData = this.yearForm.value;

    this.api.post('/ceremonial-years', formData).subscribe({
      next: () => {
        this.toastr.success('Année créée avec succès');
        this.router.navigate(['/years']);
      },
      error: () => {
        this.toastr.error('Erreur lors de l\'enregistrement');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/years']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get f() { return this.yearForm.controls; }
}