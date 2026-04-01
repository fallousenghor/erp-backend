import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MediasService } from '../medias.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SafePipe } from '../../../shared/pipes/safe.pipe';

// Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { LoadingSpinnerComponent } from '../../../shared/components/loading-spinner/loading-spinner.component';

@Component({
  selector: 'app-media-viewer',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatTooltipModule,
    SafePipe,
    LoadingSpinnerComponent
  ],
  templateUrl: './media-viewer.component.html',
  styleUrls: ['./media-viewer.component.scss']
})
export class MediaViewerComponent implements OnInit {
  media: any = null;
  loading = true;
  safeUrl: SafeResourceUrl | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private mediasService: MediasService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    const mediaId = this.route.snapshot.params['id'];
    this.loadMedia(mediaId);
  }

  loadMedia(id: number): void {
    this.loading = true;
    this.mediasService.getMediaById(id).subscribe({
      next: (response) => {
        this.media = response.data;
        this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.media.fileUrl);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  back(): void {
    this.router.navigate(['/medias']);
  }

  goBack(): void {
    this.router.navigate(['/medias']);
  }

  downloadMedia(): void {
    if (this.media) {
      const link = document.createElement('a');
      link.href = this.media.url;
      link.download = this.media.title;
      link.click();
    }
  }

  isImage(): boolean {
    return this.media?.type?.startsWith('image/');
  }

  isVideo(): boolean {
    return this.media?.type?.startsWith('video/');
  }

  isAudio(): boolean {
    return this.media?.type?.startsWith('audio/');
  }

  isPdf(): boolean {
    return this.media?.type === 'application/pdf';
  }
}
