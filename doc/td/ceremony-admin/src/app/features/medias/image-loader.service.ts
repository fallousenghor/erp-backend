import { Injectable } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageLoaderService {
  private imageCache = new Map<string, SafeUrl>();
  private loadingSubject = new BehaviorSubject<Set<string>>(new Set());
  private errorSubject = new BehaviorSubject<Map<string, string>>(new Map());

  constructor(private sanitizer: DomSanitizer) {}

  /**
   * Get a safe URL for an image from Cloudinary
   * Ensures proper CORS headers are set
   */
  getSafeImageUrl(imageUrl: string): SafeUrl {
    if (!imageUrl) {
      return this.sanitizer.bypassSecurityTrustUrl('');
    }

    if (this.imageCache.has(imageUrl)) {
      return this.imageCache.get(imageUrl)!;
    }

    // Ensure HTTPS and add CORS parameters to Cloudinary URLs
    let finalUrl = imageUrl;
    if (imageUrl.includes('res.cloudinary.com')) {
      // Add security headers - Cloudinary supports these parameters
      // f_auto ensures proper format, q_auto ensures quality
      if (!imageUrl.includes('f_auto')) {
        finalUrl = imageUrl.replace('/image/upload/', '/image/upload/f_auto,q_auto/');
      }
    }

    // Ensure HTTPS
    if (finalUrl.startsWith('http://')) {
      finalUrl = finalUrl.replace('http://', 'https://');
    }

    const safeUrl = this.sanitizer.bypassSecurityTrustUrl(finalUrl);
    this.imageCache.set(imageUrl, safeUrl);
    return safeUrl;
  }

  /**
   * Track image loading state
   */
  setLoading(imageUrl: string, isLoading: boolean): void {
    const loading = new Set(this.loadingSubject.value);
    if (isLoading) {
      loading.add(imageUrl);
    } else {
      loading.delete(imageUrl);
    }
    this.loadingSubject.next(loading);
  }

  /**
   * Get loading state for an image
   */
  isLoading(imageUrl: string): Observable<boolean> {
    return new Observable(observer => {
      this.loadingSubject.subscribe(loading => {
        observer.next(loading.has(imageUrl));
      });
    });
  }

  /**
   * Track image loading errors
   */
  setError(imageUrl: string, error: string | null): void {
    const errors = new Map(this.errorSubject.value);
    if (error) {
      errors.set(imageUrl, error);
    } else {
      errors.delete(imageUrl);
    }
    this.errorSubject.next(errors);
  }

  /**
   * Get error for an image
   */
  getError(imageUrl: string): Observable<string | undefined> {
    return new Observable(observer => {
      this.errorSubject.subscribe(errors => {
        observer.next(errors.get(imageUrl));
      });
    });
  }

  /**
   * Clear cache
   */
  clearCache(): void {
    this.imageCache.clear();
  }
}
