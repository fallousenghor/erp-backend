import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TransactionsService, Transaction } from '../transactions.service';
import { ToastrService } from 'ngx-toastr';

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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-transactions-list',
  standalone: true,
  imports: [
    CommonModule,
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
    MatProgressSpinnerModule
  ],
  templateUrl: './transactions-list.component.html',
  styleUrls: ['./transactions-list.component.scss']
})
export class TransactionsListComponent implements OnInit {
  displayedColumns: string[] = ['transactionDate', 'type', 'description', 'category', 'amount', 'memberName', 'actions'];
  dataSource: MatTableDataSource<Transaction>;
  loading = true;
  selectedType = 'ALL';
  
  transactionTypes = [
    { value: 'ALL', label: 'Tous' },
    { value: 'ENTREE', label: 'Entrées' },
    { value: 'SORTIE', label: 'Sorties' },
    { value: 'COTISATION', label: 'Cotisations' },
    { value: 'DON', label: 'Dons' }
  ];

  // Statistiques
  stats = {
    totalIncome: 0,
    totalExpense: 0,
    balance: 0,
    transactionCount: 0
  };

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private transactionsService: TransactionsService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.dataSource = new MatTableDataSource<Transaction>([]);
  }

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.loading = true;
    
    const request = this.selectedType === 'ALL'
      ? this.transactionsService.getTransactions()
      : this.transactionsService.getTransactionsByType(this.selectedType);

    request.subscribe({
      next: (response) => {
        this.dataSource.data = response.data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.calculateStats(response.data);
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur de chargement des transactions');
        this.loading = false;
      }
    });
  }

  calculateStats(transactions: Transaction[]): void {
    this.stats.transactionCount = transactions.length;
    this.stats.totalIncome = transactions
      .filter(t => t.type === 'ENTREE' || t.type === 'COTISATION' || t.type === 'DON')
      .reduce((sum, t) => sum + t.amount, 0);
    this.stats.totalExpense = transactions
      .filter(t => t.type === 'SORTIE')
      .reduce((sum, t) => sum + t.amount, 0);
    this.stats.balance = this.stats.totalIncome - this.stats.totalExpense;
  }

  onTypeChange(): void {
    this.loadTransactions();
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getTypeColor(type: string): string {
    switch (type) {
      case 'ENTREE': return 'primary';
      case 'SORTIE': return 'warn';
      case 'COTISATION': return 'accent';
      case 'DON': return 'primary';
      default: return '';
    }
  }

  getTypeIcon(type: string): string {
    switch (type) {
      case 'ENTREE': return 'arrow_downward';
      case 'SORTIE': return 'arrow_upward';
      case 'COTISATION': return 'payments';
      case 'DON': return 'volunteer_activism';
      default: return 'attach_money';
    }
  }

  deleteTransaction(transaction: Transaction): void {
    if (confirm(`Supprimer la transaction "${transaction.description}" ?`)) {
      this.transactionsService.deleteTransaction(transaction.id).subscribe({
        next: () => {
          this.toastr.success('Transaction supprimée');
          this.loadTransactions();
        },
        error: () => {
          this.toastr.error('Erreur lors de la suppression');
        }
      });
    }
  }

  addTransaction(): void {
    this.router.navigate(['/transactions/new']);
  }

  viewReport(): void {
    this.router.navigate(['/transactions/report']);
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF'
    }).format(value);
  }
}
