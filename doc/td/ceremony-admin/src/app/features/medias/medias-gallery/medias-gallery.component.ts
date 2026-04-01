import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MediasService, Media } from '../medias.service';
import { ImageLoaderService } from '../image-loader.service';
import { ToastrService } from 'ngx-toastr';
import { SafeUrl } from '@angular/platform-browser';
import { ApiService } from '../../../core/services/api.service';

// Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-medias-gallery',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './medias-gallery.component.html',
  styleUrls: ['./medias-gallery.component.scss']
})
export class MediasGalleryComponent implements OnInit {
  medias: Media[] = [];
  filteredMedias: Media[] = [];
  loading = true;
  selectedType = 'ALL';
  searchQuery = '';
  selectedYear: number | null = null;
  ceremonialYears: any[] = [];
  activeYearId: number | null = null;

  mediaTypes = [
    { value: 'ALL', label: 'Tous', icon: 'perm_media' },
    { value: 'PHOTO', label: 'Photos', icon: 'photo' },
    { value: 'VIDEO', label: 'Vidéos', icon: 'videocam' },
    { value: 'AUDIO', label: 'Audios', icon: 'audiotrack' }
  ];

  constructor(
    private mediasService: MediasService,
    private imageLoaderService: ImageLoaderService,
    private router: Router,
    private toastr: ToastrService,
    private api: ApiService
  ) {}

  ngOnInit(): void {
    this.loadActiveYear();
  }

  loadActiveYear(): void {
    // First get the active ceremonial year information
    this.api.get('/ceremonial-years/active').subscribe({
      next: (response: any) => {
        this.activeYearId = response.data.id;
        this.loadMedias();
      },
      error: () => {
        this.toastr.error('Erreur de chargement de l\'année active');
        // Continue loading media anyway, but without year filtering
        this.activeYearId = null;
        this.loadMedias();
      }
    });
  }

  loadMedias(): void {
    this.loading = true;
    this.mediasService.getMedias().subscribe({
      next: (response) => {
        this.medias = response.data;
        this.applyFilters();
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Erreur de chargement des médias');
        this.loading = false;
      }
    });
  }

  onTypeChange(): void {
    if (this.selectedType === 'ALL') {
      this.loadMedias();
    } else {
      this.loading = true;
      this.mediasService.getMediasByType(this.selectedType).subscribe({
        next: (response) => {
          // Filter by active year
          this.medias = response.data.filter((media: Media) =>
            this.activeYearId ? media.ceremonialYearId === this.activeYearId : true
          );
          this.applyFilters();
          this.loading = false;
        },
        error: () => {
          this.toastr.error('Erreur de chargement des médias par type');
          this.loading = false;
        }
      });
    }
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.loading = true;
      this.mediasService.searchMedias(this.searchQuery).subscribe({
        next: (response) => {
          // Filter by active year
          this.filteredMedias = response.data.filter((media: Media) =>
            this.activeYearId ? media.ceremonialYearId === this.activeYearId : true
          );
          this.loading = false;
        },
        error: () => {
          this.toastr.error('Erreur lors de la recherche');
          this.loading = false;
        }
      });
    } else {
      this.applyFilters();
    }
  }

  applyFilters(): void {
    this.filteredMedias = this.medias;
  }

  uploadMedia(): void {
    this.router.navigate(['/medias/upload']);
  }

  viewMedia(media: Media): void {
    this.router.navigate(['/medias', media.id]);
  }

  deleteMedia(media: Media, event: Event): void {
    event.stopPropagation();
    if (confirm(`Supprimer "${media.title}" ?`)) {
      this.mediasService.deleteMedia(media.id).subscribe({
        next: () => {
          this.toastr.success('Média supprimé');
          this.loadMedias();
        },
        error: () => {
          this.toastr.error('Erreur lors de la suppression');
        }
      });
    }
  }

  downloadMedia(media: Media, event: Event): void {
    event.stopPropagation();
    this.mediasService.downloadMedia(media.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = media.title;
        link.click();
        window.URL.revokeObjectURL(url);
        this.toastr.success('Téléchargement démarré');
      },
      error: () => {
        this.toastr.error('Erreur lors du téléchargement');
      }
    });
  }

  getMediaIcon(type: string): string {
    switch (type) {
      case 'PHOTO': return 'photo';
      case 'VIDEO': return 'videocam';
      case 'AUDIO': return 'audiotrack';
      default: return 'insert_drive_file';
    }
  }

  formatFileSize(bytes: number): string {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  }

  formatDuration(seconds: number): string {
    if (!seconds) return '';
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes}:${secs.toString().padStart(2, '0')}`;
  }

  /**
   * Get safe image URL with proper CORS handling for Cloudinary
   */
  getSafeImageUrl(imageUrl: string): SafeUrl {
    return this.imageLoaderService.getSafeImageUrl(imageUrl);
  }

  /**
   * Handle image load error
   */
  onImageError(media: Media): void {
    this.toastr.error(`Erreur lors du chargement de l'image: ${media.title}`);
    // Try to display from thumbnail URL as fallback
    if (media.thumbnailUrl && media.thumbnailUrl !== media.fileUrl) {
      media.fileUrl = media.thumbnailUrl;
    }
  }
}