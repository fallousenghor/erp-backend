import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadComponent: () => import('./member-list/member-list.component').then(m => m.MembersListComponent) },
  { path: 'new', loadComponent: () => import('./member-form/member-form.component').then(m => m.MemberFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./member-form/member-form.component').then(m => m.MemberFormComponent) },
  { path: ':id', loadComponent: () => import('./member-detail/member-detail.component').then(m => m.MemberDetailComponent) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MembersRoutingModule { }
