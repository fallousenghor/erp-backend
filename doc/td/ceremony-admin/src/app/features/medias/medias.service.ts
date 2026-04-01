import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Media {
  id: number;
  ceremonialYearId: number;
  ceremonialYear: number;
  title: string;
  description: string;
  type: 'PHOTO' | 'VIDEO' | 'AUDIO';
  fileUrl: string;
  thumbnailUrl: string;
  fileSize: number;
  duration: number;
  uploadedByName: string;
  tags: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class MediasService {
  private baseUrl = environment.apiUrl;

  constructor(
    private api: ApiService,
    private http: HttpClient
  ) {}

  getMedias(): Observable<any> {
    return this.api.get<Media[]>('/medias/active-year');
  }

  getMediasByYear(yearId: number): Observable<any> {
    return this.api.get<Media[]>(`/medias/year/${yearId}`);
  }

  getMediasByType(type: string): Observable<any> {
    return this.api.get<Media[]>(`/medias/type/${type}`);
  }

  getMediaById(id: number): Observable<any> {
    return this.api.get<Media>(`/medias/${id}`);
  }

  searchMedias(query: string): Observable<any> {
    return this.api.get<Media[]>(`/medias/search?query=${query}`);
  }

  uploadMedia(file: File, metadata: any): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('data', new Blob([JSON.stringify(metadata)], {
      type: 'application/json'
    }));

    return this.api.uploadFile<any>('/medias', formData);
  }

  deleteMedia(id: number): Observable<any> {
    return this.api.delete(`/medias/${id}`);
  }

  downloadMedia(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/medias/${id}/download`, {
      responseType: 'blob'
    });
  }
}