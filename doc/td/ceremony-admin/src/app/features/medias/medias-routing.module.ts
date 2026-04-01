import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadComponent: () => import('./medias-gallery/medias-gallery.component').then(m => m.MediasGalleryComponent) },
  { path: 'upload', loadComponent: () => import('./media-upload/media-upload.component').then(m => m.MediaUploadComponent) },
  { path: ':id', loadComponent: () => import('./media-viewer/media-viewer.component').then(m => m.MediaViewerComponent) }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MediasRoutingModule { }
