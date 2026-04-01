import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ToastrService } from 'ngx-toastr';
import { MembersService } from '../members.service';

@Component({
  selector: 'app-member-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatDatepickerModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './member-form.component.html',
  styleUrls: ['./member-form.component.scss']
})
export class MemberFormComponent implements OnInit {
  memberForm: FormGroup;
  isEditMode = false;
  memberId: number | null = null;
  loading = false;
  selectedFile: File | null = null;
  photoPreview: string | null = null;

  constructor(
    private fb: FormBuilder,
    private membersService: MembersService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
    this.memberForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{9,15}$/)]],
      secondaryPhone: ['', [Validators.pattern(/^[0-9]{9,15}$/)]],
      dateOfBirth: [''],
      address: [''],
      emergencyContact: [''],
      emergencyPhone: ['', [Validators.pattern(/^[0-9]{9,15}$/)]],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.memberId = +params['id'];
        this.loadMember();
      }
    });
  }

  loadMember(): void {
    if (!this.memberId) return;

    this.loading = true;
    this.membersService.getMemberById(this.memberId).subscribe({
      next: (response) => {
        this.memberForm.patchValue(response.data);
        if (response.data.photoUrl) {
          this.photoPreview = response.data.photoUrl;
        }
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement du membre');
        this.loading = false;
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      
      // Prévisualisation
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.photoPreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.memberForm.invalid) {
      this.markFormGroupTouched(this.memberForm);
      return;
    }

    this.loading = true;
    const formData = this.memberForm.value;

    const request = this.isEditMode && this.memberId
      ? this.membersService.updateMember(this.memberId, formData)
      : this.membersService.createMember(formData);

    request.subscribe({
      next: (response) => {
        this.toastr.success(
          this.isEditMode ? 'Membre modifié avec succès' : 'Membre créé avec succès'
        );
        this.router.navigate(['/members']);
      },
      error: (error) => {
        this.toastr.error('Erreur lors de l\'enregistrement');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/members']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  get f() { return this.memberForm.controls; }
}