// ========================================
// 5. TRANSACTIONS MODULE
// ========================================

// src/app/features/transactions/transactions.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

// Material
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// Components
import { TransactionsListComponent } from './transactions-list/transactions-list.component';
import { TransactionFormComponent } from './transaction-form/transaction-form.component';
import { FinancialReportComponent } from './financial-report/financial-report.component';

const routes: Routes = [
  { path: '', component: TransactionsListComponent },
  { path: 'new', component: TransactionFormComponent },
  { path: 'report', component: FinancialReportComponent }
];

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),

    // Standalone Components
    TransactionsListComponent,
    TransactionFormComponent,
    FinancialReportComponent,

    // Angular Material
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatSelectModule,
    MatDatepickerModule,
    MatProgressSpinnerModule
  ],
  exports: [
    RouterModule
  ]
})
export class TransactionsModule {}
