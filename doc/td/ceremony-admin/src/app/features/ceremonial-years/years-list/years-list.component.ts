import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ApiService } from '../../../core/services/api.service';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-ceremonial-years',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatDialogModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatGridListModule,
    MatTooltipModule,
    LoadingSpinnerComponent
  ],
  templateUrl: './years-list.component.html',
  styleUrls: ['./years-list.component.scss']
})
export class CeremonialYearsComponent implements OnInit {
  years: any[] = [];
  loading = true;

  constructor(
    private api: ApiService,
    private toastr: ToastrService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadYears();
  }

  loadYears(): void {
    this.loading = true;
    this.api.get('/ceremonial-years').subscribe({
      next: (response: any) => {
        this.years = response.data;
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur de chargement');
        this.loading = false;
      }
    });
  }

  activateYear(year: any): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Activer l\'année',
        message: `Êtes-vous sûr de vouloir activer l'année ${year.theme} ? Cela désactivera automatiquement l'année active actuelle.`,
        confirmText: 'Activer'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.api.put(`/ceremonial-years/${year.id}/activate`, {}).subscribe({
          next: () => {
            this.toastr.success('Année activée avec succès');
            this.loadYears();
          },
          error: () => {
            this.toastr.error('Erreur lors de l\'activation');
          }
        });
      }
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['/years/new']);
  }

  deleteYear(year: any): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Supprimer l\'année',
        message: `Êtes-vous sûr de vouloir supprimer l'année ${year.theme} ?`,
        confirmText: 'Supprimer'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.api.delete(`/ceremonial-years/${year.id}`).subscribe({
          next: () => {
            this.toastr.success('Année supprimée');
            this.loadYears();
          },
          error: () => this.toastr.error('Erreur lors de la suppression')
        });
      }
    });
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF'
    }).format(value);
  }
}

