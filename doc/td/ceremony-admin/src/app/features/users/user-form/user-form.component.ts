import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UsersService, User, CreateUserRequest, UpdateUserRequest } from '../users.service';
import { ToastrService } from 'ngx-toastr';

// Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatSlideToggleModule
  ],
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {
  userForm: FormGroup;
  loading = false;
  isEditMode = false;
  userId: number | null = null;

  roles = [
    { value: 'ADMIN', label: 'Administrateur' },
    { value: 'USER', label: 'Utilisateur' }
  ];

  constructor(
    private fb: FormBuilder,
    private usersService: UsersService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      firstName: [''],
      lastName: [''],
      role: ['USER', Validators.required],
      status: [true]
    });
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['id'];
    this.isEditMode = !!this.userId;

    if (this.isEditMode && this.userId) {
      this.loadUser(this.userId);
    } else {
      // Add password field for new users
      this.userForm.addControl('password', this.fb.control('', [Validators.required, Validators.minLength(6)]));
    }
  }

  loadUser(id: number): void {
    this.loading = true;
    this.usersService.getUserById(id).subscribe({
      next: (response) => {
        const user = response.data;
        this.userForm.patchValue({
          username: user.username,
          email: user.email,
          firstName: user.firstName || '',
          lastName: user.lastName || '',
          role: user.role,
          status: user.status === 'ACTIVE'
        });
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur lors du chargement de l\'utilisateur');
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      this.markFormGroupTouched(this.userForm);
      return;
    }

    this.loading = true;
    const formValue = this.userForm.value;

    if (this.isEditMode && this.userId) {
      const updateData: UpdateUserRequest = {
        username: formValue.username,
        email: formValue.email,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        role: formValue.role,
        status: formValue.status ? 'ACTIVE' : 'INACTIVE'
      };

      this.usersService.updateUser(this.userId, updateData).subscribe({
        next: () => {
          this.toastr.success('Utilisateur modifié avec succès');
          this.router.navigate(['/users']);
        },
        error: () => {
          this.toastr.error('Erreur lors de la modification');
          this.loading = false;
        }
      });
    } else {
      const createData: CreateUserRequest = {
        username: formValue.username,
        email: formValue.email,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        password: formValue.password,
        role: formValue.role
      };

      this.usersService.createUser(createData).subscribe({
        next: () => {
          this.toastr.success('Utilisateur créé avec succès');
          this.router.navigate(['/users']);
        },
        error: () => {
          this.toastr.error('Erreur lors de la création');
          this.loading = false;
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/users']);
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get f() { return this.userForm.controls; }
}