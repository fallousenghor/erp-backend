import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TransactionsService } from '../transactions.service';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from '../../../core/services/api.service';

// Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-transaction-form',
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
  templateUrl: './transaction-form.component.html',
  styleUrls: ['./transaction-form.component.scss']
})
export class TransactionFormComponent implements OnInit {
  transactionForm: FormGroup;
  loading = false;
  ceremonialYears: any[] = [];
  members: any[] = [];
  
  transactionTypes = [
    { value: 'ENTREE', label: 'Entrée' },
    { value: 'SORTIE', label: 'Sortie' },
    { value: 'COTISATION', label: 'Cotisation' },
    { value: 'DON', label: 'Don' }
  ];

  categories = [
    'Logistique',
    'Nourriture',
    'Transport',
    'Équipement',
    'Personnel',
    'Communication',
    'Autre'
  ];

  paymentMethods = [
    { value: 'ESPECE', label: 'Espèces' },
    { value: 'WAVE', label: 'Wave' },
    { value: 'ORANGE_MONEY', label: 'Orange Money' },
    { value: 'VIREMENT', label: 'Virement' },
    { value: 'AUTRE', label: 'Autre' }
  ];

  constructor(
    private fb: FormBuilder,
    private transactionsService: TransactionsService,
    private api: ApiService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.transactionForm = this.fb.group({
      ceremonialYearId: ['', Validators.required],
      transactionDate: [new Date(), Validators.required],
      type: ['ENTREE', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]],
      description: ['', Validators.required],
      category: [''],
      paymentMethod: ['ESPECE'],
      referenceNumber: [''],
      memberId: [''],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadCeremonialYears();
    this.loadMembers();
  }

  loadCeremonialYears(): void {
    this.api.get('/ceremonial-years').subscribe({
      next: (response: any) => {
        this.ceremonialYears = response.data;
        // Sélectionner l'année active par défaut
        const activeYear = this.ceremonialYears.find(y => y.active);
        if (activeYear) {
          this.transactionForm.patchValue({ ceremonialYearId: activeYear.id });
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

  onSubmit(): void {
    if (this.transactionForm.invalid) {
      this.markFormGroupTouched(this.transactionForm);
      return;
    }

    this.loading = true;
    this.transactionsService.createTransaction(this.transactionForm.value).subscribe({
      next: () => {
        this.toastr.success('Transaction enregistrée avec succès');
        this.router.navigate(['/transactions']);
      },
      error: () => {
        this.toastr.error('Erreur lors de l\'enregistrement');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/transactions']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get f() { return this.transactionForm.controls; }
}