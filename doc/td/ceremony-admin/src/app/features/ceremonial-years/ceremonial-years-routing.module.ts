import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadComponent: () => import('./years-list/years-list.component').then(m => m.CeremonialYearsComponent) },
  { path: 'new', loadComponent: () => import('./ceremonial-year-form/ceremonial-year-form.component').then(m => m.CeremonialYearFormComponent) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CeremonialYearsRoutingModule { }
