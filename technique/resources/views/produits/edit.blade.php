@extends('layouts.app')

@section('title', 'Modifier le produit')

@section('content')
<div class="mb-4">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a href="{{ route('produits.index') }}">Produits</a>
            </li>
            <li class="breadcrumb-item">
                <a href="{{ route('produits.show', $produit) }}">{{ $produit->nom }}</a>
            </li>
            <li class="breadcrumb-item active">Modifier</li>
        </ol>
    </nav>
    
    <h1 class="h3">
        <i class="fas fa-edit me-2 text-primary"></i>
        Modifier le produit
    </h1>
</div>

<div class="row">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-body">
                <form action="{{ route('produits.update', $produit) }}" 
                      method="POST" 
                      enctype="multipart/form-data"
                      id="produit-form">
                    @csrf
                    @method('PUT')
                    
                    <!-- Informations de base -->
                    <h5 class="card-title mb-3">
                        <i class="fas fa-info-circle me-2"></i>
                        Informations de base
                    </h5>
                    
                    <div class="row mb-3">
                        <div class="col-md-8">
                            <label for="nom" class="form-label">
                                Nom du produit <span class="text-danger">*</span>
                            </label>
                            <input type="text" 
                                   class="form-control @error('nom') is-invalid @enderror" 
                                   id="nom" 
                                   name="nom" 
                                   value="{{ old('nom', $produit->nom) }}"
                                   required>
                            @error('nom')
                                <div class="invalid-feedback">{{ $message }}</div>
                            @enderror
                        </div>
                        
                        <div class="col-md-4">
                            <label for="reference" class="form-label">Référence</label>
                            <input type="text" 
                                   class="form-control @error('reference') is-invalid @enderror" 
                                   id="reference" 
                                   name="reference" 
                                   value="{{ old('reference', $produit->reference) }}">
                            @error('reference')
                                <div class="invalid-feedback">{{ $message }}</div>
                            @enderror
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control @error('description') is-invalid @enderror" 
                                  id="description" 
                                  name="description" 
                                  rows="4">{{ old('description', $produit->description) }}</textarea>
                        @error('description')
                            <div class="invalid-feedback">{{ $message }}</div>
                        @enderror
                    </div>
                    
                    <div class="mb-3">
                        <label for="categorie_id" class="form-label">
                            Catégorie <span class="text-danger">*</span>
                        </label>
                        <select class="form-select @error('categorie_id') is-invalid @enderror" 
                                id="categorie_id" 
                                name="categorie_id" 
                                required>
                            <option value="">-- Sélectionnez une catégorie --</option>
                            @foreach($categories as $categorie)
                                <option value="{{ $categorie->id }}" 
                                        {{ old('categorie_id', $produit->categorie_id) == $categorie->id ? 'selected' : '' }}>
                                    {{ $categorie->nom }}
                                </option>
                            @endforeach
                        </select>
                        @error('categorie_id')
                            <div class="invalid-feedback">{{ $message }}</div>
                        @enderror
                    </div>
                    
                    <hr class="my-4">
                    
                    <!-- Prix et stock -->
                    <h5 class="card-title mb-3">
                        <i class="fas fa-euro-sign me-2"></i>
                        Prix et stock
                    </h5>
                    
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="prix" class="form-label">
                                Prix (€) <span class="text-danger">*</span>
                            </label>
                            <div class="input-group">
                                <input type="number" 
                                       class="form-control @error('prix') is-invalid @enderror" 
                                       id="prix" 
                                       name="prix" 
                                       value="{{ old('prix', $produit->prix) }}"
                                       step="0.01"
                                       min="0"
                                       required>
                                <span class="input-group-text">€</span>
                                @error('prix')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>
                        
                        <div class="col-md-6">
                            <label for="stock" class="form-label">
                                Quantité en stock <span class="text-danger">*</span>
                            </label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="fas fa-boxes"></i>
                                </span>
                                <input type="number" 
                                       class="form-control @error('stock') is-invalid @enderror" 
                                       id="stock" 
                                       name="stock" 
                                       value="{{ old('stock', $produit->stock) }}"
                                       min="0"
                                       required>
                                @error('stock')
                                    <div class="invalid-feedback">{{ $message }}</div>
                                @enderror
                            </div>
                        </div>
                    </div>
                    
                    <hr class="my-4">
                    
                    <!-- Promotion -->
                    <h5 class="card-title mb-3">
                        <i class="fas fa-percent me-2"></i>
                        Promotion
                    </h5>
                    
                    <div class="mb-3">
                        <div class="form-check form-switch">
                            <input class="form-check-input" 
                                   type="checkbox" 
                                   id="en_promotion" 
                                   name="en_promotion"
                                   value="1"
                                   {{ old('en_promotion', $produit->en_promotion) ? 'checked' : '' }}
                                   onchange="togglePromotion()">
                            <label class="form-check-label" for="en_promotion">
                                Activer une promotion
                            </label>
                        </div>
                    </div>
                    
                    <div id="promotion-fields" style="display: {{ old('en_promotion', $produit->en_promotion) ? 'block' : 'none' }};">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="prix_promotion" class="form-label">
                                    Prix promotionnel (€)
                                </label>
                                <div class="input-group">
                                    <input type="number" 
                                           class="form-control @error('prix_promotion') is-invalid @enderror" 
                                           id="prix_promotion" 
                                           name="prix_promotion" 
                                           value="{{ old('prix_promotion', $produit->prix_promotion) }}"
                                           step="0.01"
                                           min="0">
                                    <span class="input-group-text">€</span>
                                    @error('prix_promotion')
                                        <div class="invalid-feedback">{{ $message }}</div>
                                    @enderror
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <label class="form-label">Aperçu de la réduction</label>
                                <div class="alert alert-info mb-0" id="reduction-preview">
                                    <i class="fas fa-calculator me-2"></i>
                                    Calcul automatique
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <hr class="my-4">
                    
                    <!-- Image -->
                    <h5 class="card-title mb-3">
                        <i class="fas fa-image me-2"></i>
                        Image du produit
                    </h5>
                    
                    @if($produit->image)
                        <div class="mb-3">
                            <label class="form-label">Image actuelle</label>
                            <div>
                                <img src="{{ asset('storage/' . $produit->image) }}" 
                                     alt="{{ $produit->nom }}"
                                     class="img-thumbnail"
                                     style="max-width: 300px;">
                            </div>
                        </div>
                    @endif
                    
                    <div class="mb-3">
                        <label for="image" class="form-label">
                            {{ $produit->image ? 'Changer l\'image' : 'Ajouter une image' }}
                        </label>
                        <input type="file" 
                               class="form-control @error('image') is-invalid @enderror" 
                               id="image" 
                               name="image"
                               accept="image/*"
                               onchange="previewImage(this)">
                        @error('image')
                            <div class="invalid-feedback">{{ $message }}</div>
                        @enderror
                        <small class="form-text text-muted">
                            {{ $produit->image ? 'Laissez vide pour conserver l\'image actuelle' : '' }}
                            Formats : JPEG, PNG, GIF, WebP (Max: 2 Mo)
                        </small>
                    </div>
                    
                    <div id="image-preview" class="mt-3" style="display: none;">
                        <label class="form-label">Nouvelle image</label>
                        <img id="preview" src="" alt="Aperçu" class="img-thumbnail" style="max-width: 300px;">
                    </div>
                    
                    <hr class="my-4">
                    
                    <!-- Statut -->
                    <h5 class="card-title mb-3">
                        <i class="fas fa-toggle-on me-2"></i>
                        Statut
                    </h5>
                    
                    <div class="mb-3">
                        <div class="form-check form-switch">
                            <input class="form-check-input" 
                                   type="checkbox" 
                                   id="actif" 
                                   name="actif"
                                   value="1"
                                   {{ old('actif', $produit->actif) ? 'checked' : '' }}>
                            <label class="form-check-label" for="actif">
                                Produit actif (visible sur le site)
                            </label>
                        </div>
                    </div>
                    
                    <!-- Boutons d'action -->
                    <div class="d-flex gap-2 mt-4">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-2"></i>
                            Enregistrer les modifications
                        </button>
                        <a href="{{ route('produits.show', $produit) }}" class="btn btn-secondary">
                            <i class="fas fa-times me-2"></i>
                            Annuler
                        </a>
                        <button type="button" 
                                class="btn btn-danger ms-auto"
                                onclick="confirmDelete(document.getElementById('delete-form'))">
                            <i class="fas fa-trash me-2"></i>
                            Supprimer
                        </button>
                    </div>
                </form>
                
                <!-- Formulaire de suppression -->
                <form id="delete-form" 
                      action="{{ route('produits.destroy', $produit) }}" 
                      method="POST" 
                      class="d-none">
                    @csrf
                    @method('DELETE')
                </form>
            </div>
        </div>
    </div>
    
    <!-- Informations complémentaires -->
    <div class="col-lg-4">
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">
                    <i class="fas fa-info-circle me-2"></i>
                    Informations
                </h5>
                
                <div class="mb-3">
                    <small class="text-muted d-block">Créé le</small>
                    <strong>{{ $produit->created_at->format('d/m/Y à H:i') }}</strong>
                </div>
                
                <div class="mb-3">
                    <small class="text-muted d-block">Dernière modification</small>
                    <strong>{{ $produit->updated_at->format('d/m/Y à H:i') }}</strong>
                </div>
                
                <div>
                    <small class="text-muted d-block">Slug</small>
                    <code>{{ $produit->slug }}</code>
                </div>
            </div>
        </div>
        
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">
                    <i class="fas fa-chart-bar me-2"></i>
                    Statistiques
                </h5>
                
                <div class="mb-3">
                    <small class="text-muted d-block">Valeur du stock</small>
                    <h4 class="mb-0">
                        {{ number_format($produit->prix * $produit->stock, 2) }} €
                    </h4>
                </div>
                
                <div>
                    <small class="text-muted d-block">Statut du stock</small>
                    <span class="badge {{ $produit->stock_badge_class }}">
                        {{ $produit->statut_stock }}
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection

@push('scripts')
<script>
    // Même script que create.blade.php pour la cohérence
    function previewImage(input) {
        const preview = document.getElementById('image-preview');
        const previewImg = document.getElementById('preview');
        
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.src = e.target.result;
                preview.style.display = 'block';
            };
            reader.readAsDataURL(input.files[0]);
        } else {
            preview.style.display = 'none';
        }
    }
    
    function togglePromotion() {
        const checkbox = document.getElementById('en_promotion');
        const fields = document.getElementById('promotion-fields');
        const prixPromo = document.getElementById('prix_promotion');
        
        if (checkbox.checked) {
            fields.style.display = 'block';
            prixPromo.required = true;
        } else {
            fields.style.display = 'none';
            prixPromo.required = false;
            prixPromo.value = '';
            updateReductionPreview();
        }
    }
    
    function updateReductionPreview() {
        const prix = parseFloat(document.getElementById('prix').value) || 0;
        const prixPromo = parseFloat(document.getElementById('prix_promotion').value) || 0;
        const preview = document.getElementById('reduction-preview');
        
        if (prix > 0 && prixPromo > 0 && prixPromo < prix) {
            const reduction = ((prix - prixPromo) / prix * 100).toFixed(0);
            const economie = (prix - prixPromo).toFixed(2);
            
            preview.innerHTML = `
                <i class="fas fa-tag me-2"></i>
                <strong>-${reduction}%</strong> de réduction
                <br>
                <small>Économie : ${economie} €</small>
            `;
            preview.className = 'alert alert-success mb-0';
        } else if (prixPromo >= prix && prixPromo > 0) {
            preview.innerHTML = `
                <i class="fas fa-exclamation-triangle me-2"></i>
                Le prix promotionnel doit être inférieur au prix normal
            `;
            preview.className = 'alert alert-warning mb-0';
        } else {
            preview.innerHTML = `
                <i class="fas fa-calculator me-2"></i>
                Calcul automatique
            `;
            preview.className = 'alert alert-info mb-0';
        }
    }
    
    document.getElementById('prix').addEventListener('input', updateReductionPreview);
    document.getElementById('prix_promotion').addEventListener('input', updateReductionPreview);
    
    // Initialiser l'aperçu au chargement
    updateReductionPreview();
    
    // Raccourcis clavier
    document.addEventListener('keydown', function(e) {
        if (e.ctrlKey && e.key === 's') {
            e.preventDefault();
            document.getElementById('produit-form').submit();
        }
    });
</script>
@endpush