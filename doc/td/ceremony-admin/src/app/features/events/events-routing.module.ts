import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadComponent: () => import('./events-list/events-list.component').then(m => m.EventsListComponent) },
  { path: 'new', loadComponent: () => import('./event-form/event-form.component').then(m => m.EventFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./event-form/event-form.component').then(m => m.EventFormComponent) },
  { path: ':id', loadComponent: () => import('./event-detail/event-detail.component').then(m => m.EventDetailComponent) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EventsRoutingModule { }
