import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadComponent: () => import('./contributions-list/contributions-list.component').then(m => m.ContributionsListComponent) },
  { path: 'new', loadComponent: () => import('./contribution-form/contribution-form.component').then(m => m.ContributionFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./contribution-form/contribution-form.component').then(m => m.ContributionFormComponent) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContributionsRoutingModule { }
