import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MaterialsService, Material } from '../materials.service';

@Component({
  selector: 'app-materials-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDialogModule,
    LoadingSpinnerComponent
  ],
  templateUrl: './materials-list.component.html',
  styleUrl: './materials-list.component.scss'
})
export class MaterialsListComponent implements OnInit {
  materials: Material[] = [];
  loading = true;
  displayedColumns = ['name', 'category', 'quantity', 'status', 'actions'];

  constructor(
    private materialsService: MaterialsService,
    private dialog: MatDialog,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadMaterials();
  }

  loadMaterials(): void {
    this.loading = true;
    this.materialsService.getMaterials().subscribe({
      next: (data) => {
        this.materials = data.data || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement du matériel', err);
        this.toastr.error('Erreur lors du chargement du matériel');
        this.loading = false;
      }
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['/materials/new']);
  }

  editMaterial(id: number): void {
    this.router.navigate([`/materials/edit/${id}`]);
  }

  deleteMaterial(material: Material): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Supprimer le matériel',
        message: `Êtes-vous sûr de vouloir supprimer "${material.name}" ?`,
        confirmText: 'Supprimer'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.materialsService.deleteMaterial(material.id).subscribe({
          next: () => {
            this.toastr.success('Matériel supprimé avec succès');
            this.loadMaterials();
          },
          error: () => this.toastr.error('Erreur lors de la suppression')
        });
      }
    });
  }

  getStatusColor(status: string): string {
    const colors: { [key: string]: string } = {
      'BON_ETAT': 'accent',
      'EN_REPARATION': 'warn',
      'PERDU': 'warn',
      'HORS_SERVICE': 'warn'
    };
    return colors[status] || 'primary';
  }
}
