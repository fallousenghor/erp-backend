import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MediasService } from '../medias.service';
import { ApiService } from '../../../core/services/api.service';
import { ToastrService } from 'ngx-toastr';

// Material
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-media-upload',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    MatProgressBarModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './media-upload.component.html',
  styleUrls: ['./media-upload.component.scss']
})
export class MediaUploadComponent implements OnInit {
  uploadForm: FormGroup;
  selectedFile: File | null = null;
  filePreview: string | null = null;
  uploading = false;
  uploadProgress = 0;
  ceremonialYears: any[] = [];
  
  mediaTypes = [
    { value: 'PHOTO', label: 'Photo', accept: 'image/*' },
    { value: 'VIDEO', label: 'Vidéo', accept: 'video/*' },
    { value: 'AUDIO', label: 'Audio', accept: 'audio/*' }
  ];

  constructor(
    private fb: FormBuilder,
    private mediasService: MediasService,
    private api: ApiService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.uploadForm = this.fb.group({
      ceremonialYearId: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      type: ['PHOTO', Validators.required],
      tags: ['']
    });
  }

  ngOnInit(): void {
    this.loadCeremonialYears();
  }

  loadCeremonialYears(): void {
    this.api.get('/ceremonial-years').subscribe({
      next: (response: any) => {
        this.ceremonialYears = response.data;
        const activeYear = this.ceremonialYears.find(y => y.active);
        if (activeYear) {
          this.uploadForm.patchValue({ ceremonialYearId: activeYear.id });
        }
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;

      // Prévisualisation pour images
      if (file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.filePreview = e.target.result;
        };
        reader.readAsDataURL(file);
      } else {
        this.filePreview = null;
      }

      // Auto-remplir le titre avec le nom du fichier
      if (!this.uploadForm.value.title) {
        const filename = file.name.replace(/\.[^/.]+$/, '');
        this.uploadForm.patchValue({ title: filename });
      }
    }
  }

  getAcceptedFileTypes(): string {
    const type = this.uploadForm.value.type;
    const mediaType = this.mediaTypes.find(t => t.value === type);
    return mediaType?.accept || '*';
  }

  onSubmit(): void {
    if (this.uploadForm.invalid || !this.selectedFile) {
      this.toastr.warning('Veuillez remplir tous les champs et sélectionner un fichier');
      return;
    }

    this.uploading = true;
    const metadata = this.uploadForm.value;

    this.mediasService.uploadMedia(this.selectedFile, metadata).subscribe({
      next: (response) => {
        this.toastr.success('Média uploadé avec succès');
        this.router.navigate(['/medias']);
      },
      error: (error) => {
        this.toastr.error('Erreur lors de l\'upload');
        this.uploading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/medias']);
  }

  get f() { return this.uploadForm.controls; }
}
