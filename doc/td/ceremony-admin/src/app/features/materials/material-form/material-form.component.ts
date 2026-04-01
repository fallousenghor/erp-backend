import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MaterialsService, Material } from '../materials.service';
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
  selector: 'app-material-form',
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
  templateUrl: './material-form.component.html',
  styleUrls: ['./material-form.component.scss']
})
export class MaterialFormComponent implements OnInit {
  materialForm: FormGroup;
  loading = false;
  isEditMode = false;
  materialId: number | null = null;

  categories = [
    'Équipement',
    'Mobilier',
    'Électronique',
    'Outils',
    'Décoration',
    'Autre'
  ];

  statuses = [
    { value: 'BON_ETAT', label: 'Bon état' },
    { value: 'EN_REPARATION', label: 'En réparation' },
    { value: 'PERDU', label: 'Perdu' },
    { value: 'HORS_SERVICE', label: 'Hors service' }
  ];

  constructor(
    private fb: FormBuilder,
    private materialsService: MaterialsService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.materialForm = this.fb.group({
      name: ['', [Validators.required]],
      description: [''],
      category: ['', Validators.required],
      referenceNumber: [''],
      quantity: [1, [Validators.required, Validators.min(1)]],
      status: ['BON_ETAT', Validators.required],
      purchaseDate: [''],
      location: [''],
      photoUrl: ['']
    });
  }

  ngOnInit(): void {
    this.materialId = this.route.snapshot.params['id'];
    this.isEditMode = !!this.materialId;

    if (this.isEditMode && this.materialId) {
      this.loadMaterial(this.materialId);
    }
  }

  loadMaterial(id: number): void {
    this.loading = true;
    this.materialsService.getMaterialById(id).subscribe({
      next: (response) => {
        const material = response.data;
        this.materialForm.patchValue({
          name: material.name,
          description: material.description,
          category: material.category,
          referenceNumber: material.referenceNumber,
          quantity: material.quantity,
          status: material.status,
          purchaseDate: material.purchaseDate ? new Date(material.purchaseDate) : '',
          location: material.location,
          photoUrl: material.photoUrl
        });
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement du matériel');
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.materialForm.invalid) {
      this.markFormGroupTouched(this.materialForm);
      return;
    }

    this.loading = true;
    const formValue = this.materialForm.value;

    if (this.isEditMode && this.materialId) {
      this.materialsService.updateMaterial(this.materialId, formValue).subscribe({
        next: () => {
          this.toastr.success('Matériel modifié avec succès');
          this.router.navigate(['/materials']);
        },
        error: () => {
          this.toastr.error('Erreur lors de la modification');
          this.loading = false;
        }
      });
    } else {
      this.materialsService.createMaterial(formValue).subscribe({
        next: () => {
          this.toastr.success('Matériel créé avec succès');
          this.router.navigate(['/materials']);
        },
        error: () => {
          this.toastr.error('Erreur lors de la création');
          this.loading = false;
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/materials']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get f() { return this.materialForm.controls; }
}