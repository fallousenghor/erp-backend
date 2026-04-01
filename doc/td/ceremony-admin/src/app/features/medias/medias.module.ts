import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

// Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';

// Components
import { MediasGalleryComponent } from './medias-gallery/medias-gallery.component';
import { MediaUploadComponent } from './media-upload/media-upload.component';
import { MediaViewerComponent } from './media-viewer/media-viewer.component';

const routes: Routes = [
  { path: '', component: MediasGalleryComponent },
  { path: 'upload', component: MediaUploadComponent },
  { path: ':id', component: MediaViewerComponent }
];

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forChild(routes),

    // Standalone Components
    MediasGalleryComponent,
    MediaUploadComponent,
    MediaViewerComponent,

    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatProgressBarModule
  ]
})
export class MediasModule { }