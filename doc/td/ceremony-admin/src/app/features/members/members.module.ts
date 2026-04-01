// src/app/features/members/members.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDividerModule } from '@angular/material/divider';

// Components
import { MemberFormComponent } from './member-form/member-form.component';
import { MemberDetailComponent } from './member-detail/member-detail.component';
import { MembersListComponent } from './member-list/member-list.component';

const routes: Routes = [
  { path: '', component: MembersListComponent },
  { path: 'new', component: MemberFormComponent },
  { path: 'edit/:id', component: MemberFormComponent },
  { path: ':id', component: MemberDetailComponent }
];

@NgModule({
  declarations: [],
  imports: [
    MembersListComponent,
    MemberFormComponent,
    MemberDetailComponent,
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forChild(routes),
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    MatDividerModule
  ]
})
export class MembersModule { }