import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ContributionsService, Contribution } from '../contributions.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { CurrencyXofPipe } from '../../../shared/pipes/currency-xof.pipe';

@Component({
  selector: 'app-contributions-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDialogModule,
    LoadingSpinnerComponent,
    CurrencyXofPipe
  ],
  templateUrl: './contributions-list.component.html',
  styleUrls: ['./contributions-list.component.scss']
})
export class ContributionsListComponent implements OnInit {
  displayedColumns: string[] = ['member', 'amount', 'date', 'status', 'actions'];
  dataSource: MatTableDataSource<Contribution>;
  contributions: Contribution[] = [];
  filteredContributions: Contribution[] = [];
  loading = true;
  searchTerm = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private contributionsService: ContributionsService,
    private router: Router,
    private dialog: MatDialog,
    private toastr: ToastrService
  ) {
    this.dataSource = new MatTableDataSource<Contribution>([]);
  }

  ngOnInit(): void {
    this.loadContributions();
  }

  loadContributions(): void {
    this.loading = true;
    this.contributionsService.getContributions().subscribe({
      next: (data: any) => {
        this.contributions = Array.isArray(data) ? data : (data.data || []);
        this.filteredContributions = this.contributions;
        this.dataSource.data = this.contributions;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur de chargement');
        this.loading = false;
      }
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['/contributions/new']);
  }

  editContribution(id: number): void {
    this.router.navigate([`/contributions/edit/${id}`]);
  }

  deleteContribution(contribution: Contribution): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Supprimer la cotisation',
        message: 'Êtes-vous sûr de vouloir supprimer cette cotisation ?',
        confirmText: 'Supprimer'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.contributionsService.deleteContribution(contribution.id).subscribe({
          next: () => {
            this.toastr.success('Cotisation supprimée');
            this.loadContributions();
          },
          error: () => this.toastr.error('Erreur lors de la suppression')
        });
      }
    });
  }

  applyFilter(event: any): void {
    const filterValue = event.target.value.toLowerCase();
    this.filteredContributions = this.contributions.filter(c =>
      c.memberName?.toLowerCase().includes(filterValue)
    );
    this.dataSource.data = this.filteredContributions;
  }
}
