import { Injectable } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Observable } from 'rxjs';

export interface Material {
  id: number;
  name: string;
  description: string;
  category: string;
  referenceNumber: string;
  quantity: number;
  availableQuantity: number;
  borrowedQuantity: number;
  status: 'BON_ETAT' | 'EN_REPARATION' | 'PERDU' | 'HORS_SERVICE';
  purchaseDate: string;
  location: string;
  photoUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class MaterialsService {
  constructor(private api: ApiService) {}

  getMaterials(): Observable<any> {
    return this.api.get<Material[]>('/materials');
  }

  getMaterialById(id: number): Observable<any> {
    return this.api.get<Material>(`/materials/${id}`);
  }

  getLowStockMaterials(threshold: number = 5): Observable<any> {
    return this.api.get<Material[]>(`/materials/low-stock?threshold=${threshold}`);
  }

  searchMaterials(query: string): Observable<any> {
    return this.api.get<Material[]>(`/materials/search?query=${query}`);
  }

  createMaterial(material: any): Observable<any> {
    return this.api.post('/materials', material);
  }

  updateMaterial(id: number, material: any): Observable<any> {
    return this.api.put(`/materials/${id}`, material);
  }

  deleteMaterial(id: number): Observable<any> {
    return this.api.delete(`/materials/${id}`);
  }
}
