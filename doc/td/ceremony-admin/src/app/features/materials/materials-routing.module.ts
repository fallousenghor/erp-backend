import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadComponent: () => import('./materials-list/materials-list.component').then(m => m.MaterialsListComponent) },
  { path: 'new', loadComponent: () => import('./material-form/material-form.component').then(m => m.MaterialFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./material-form/material-form.component').then(m => m.MaterialFormComponent) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MaterialsRoutingModule { }
