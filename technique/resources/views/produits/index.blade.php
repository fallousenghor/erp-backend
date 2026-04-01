@extends('layouts.app')

@section('title', 'Liste des produits')

@section('content')
<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h3 mb-0">
        <i class="fas fa-boxes me-2 text-primary"></i>
        Gestion des Produits
    </h1>
    <a href="{{ route('produits.create') }}" class="btn btn-primary">
        <i class="fas fa-plus me-2"></i>
        Nouveau Produit
    </a>
</div>

<!-- Statistiques -->
<div class="row mb-4">
    <div class="col-md-3 mb-3">
        <div class="card stat-card primary">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <p class="text-muted mb-1 small">Total Produits</p>
                        <h3 class="mb-0">{{ $statistiques['total'] }}</h3>
                    </div>
                    <div class="text-primary">
                        <i class="fas fa-box fa-2x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-md-3 mb-3">
        <div class="card stat-card success">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <p class="text-muted mb-1 small">En Stock</p>
                        <h3 class="mb-0">{{ $statistiques['en_stock'] }}</h3>
                    </div>
                    <div class="text-success">
                        <i class="fas fa-check-circle fa-2x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-md-3 mb-3">
        <div class="card stat-card warning">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <p class="text-muted mb-1 small">En Promotion</p>
                        <h3 class="mb-0">{{ $statistiques['en_promotion'] }}</h3>
                    </div>
                    <div class="text-warning">
                        <i class="fas fa-percent fa-2x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-md-3 mb-3">
        <div class="card stat-card danger">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <p class="text-muted mb-1 small">Rupture Stock</p>
                        <h3 class="mb-0">{{ $statistiques['rupture_stock'] }}</h3>
                    </div>
                    <div class="text-danger">
                        <i class="fas fa-exclamation-triangle fa-2x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Filtres et recherche -->
<div class="card mb-4">
    <div class="card-body">
        <form method="GET" action="{{ route('produits.index') }}" class="row g-3">
            <div class="col-md-4">
                <label for="search" class="form-label">Recherche</label>
                <div class="input-group">
                    <span class="input-group-text">
                        <i class="fas fa-search"></i>
                    </span>
                    <input type="text" 
                           class="form-control" 
                           id="search" 
                           name="search" 
                           value="{{ $filters['search'] ?? '' }}"
                           placeholder="Nom, référence, description...">
                </div>
            </div>
            
            <div class="col-md-3">
                <label for="categorie_id" class="form-label">Catégorie</label>
                <select name="categorie_id" id="categorie_id" class="form-select">
                    <option value="">Toutes les catégories</option>
                    @foreach($categories as $categorie)
                        <option value="{{ $categorie->id }}" 
                                {{ ($filters['categorie_id'] ?? '') == $categorie->id ? 'selected' : '' }}>
                            {{ $categorie->nom }}
                        </option>
                    @endforeach
                </select>
            </div>
            
            <div class="col-md-2">
                <label for="sort" class="form-label">Trier par</label>
                <select name="sort" id="sort" class="form-select">
                    <option value="recent" {{ ($filters['sort'] ?? 'recent') == 'recent' ? 'selected' : '' }}>
                        Plus récents
                    </option>
                    <option value="nom" {{ ($filters['sort'] ?? '') == 'nom' ? 'selected' : '' }}>
                        Nom (A-Z)
                    </option>
                    <option value="prix_asc" {{ ($filters['sort'] ?? '') == 'prix_asc' ? 'selected' : '' }}>
                        Prix croissant
                    </option>
                    <option value="prix_desc" {{ ($filters['sort'] ?? '') == 'prix_desc' ? 'selected' : '' }}>
                        Prix décroissant
                    </option>
                    <option value="stock" {{ ($filters['sort'] ?? '') == 'stock' ? 'selected' : '' }}>
                        Stock
                    </option>
                </select>
            </div>
            
            <div class="col-md-3">
                <label class="form-label d-block">Filtres rapides</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" 
                           type="checkbox" 
                           name="en_stock" 
                           id="en_stock" 
                           value="1"
                           {{ !empty($filters['en_stock']) ? 'checked' : '' }}>
                    <label class="form-check-label" for="en_stock">
                        En stock
                    </label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" 
                           type="checkbox" 
                           name="en_promotion" 
                           id="en_promotion" 
                           value="1"
                           {{ !empty($filters['en_promotion']) ? 'checked' : '' }}>
                    <label class="form-check-label" for="en_promotion">
                        Promos
                    </label>
                </div>
            </div>
            
            <div class="col-12">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-filter me-2"></i>
                    Filtrer
                </button>
                <a href="{{ route('produits.index') }}" class="btn btn-secondary">
                    <i class="fas fa-times me-2"></i>
                    Réinitialiser
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Liste des produits -->
<div class="card">
    <div class="card-body">
        @if($produits->isEmpty())
            <div class="text-center py-5">
                <i class="fas fa-box-open fa-4x text-muted mb-3"></i>
                <p class="text-muted">Aucun produit trouvé.</p>
                <a href="{{ route('produits.create') }}" class="btn btn-primary">
                    <i class="fas fa-plus me-2"></i>
                    Créer un produit
                </a>
            </div>
        @else
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th style="width: 80px;">Image</th>
                            <th>Nom</th>
                            <th>Catégorie</th>
                            <th>Référence</th>
                            <th>Prix</th>
                            <th>Stock</th>
                            <th>Statut</th>
                            <th class="text-end" style="width: 200px;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($produits as $produit)
                            <tr>
                                <td>
                                    @if($produit->image)
                                        <img src="{{ asset('storage/' . $produit->image) }}" 
                                             alt="{{ $produit->nom }}"
                                             class="product-image-thumbnail">
                                    @else
                                        <div class="product-image-thumbnail bg-light d-flex align-items-center justify-content-center">
                                            <i class="fas fa-image text-muted"></i>
                                        </div>
                                    @endif
                                </td>
                                <td>
                                    <div class="fw-semibold">{{ $produit->nom }}</div>
                                    @if($produit->en_promotion)
                                        <span class="badge bg-warning text-dark">
                                            <i class="fas fa-percent me-1"></i>
                                            -{{ $produit->pourcentage_reduction }}%
                                        </span>
                                    @endif
                                </td>
                                <td>
                                    <span class="badge bg-secondary">
                                        {{ $produit->categorie->nom }}
                                    </span>
                                </td>
                                <td>
                                    <code>{{ $produit->reference }}</code>
                                </td>
                                <td>
                                    @if($produit->en_promotion)
                                        <div>
                                            <small class="text-muted text-decoration-line-through">
                                                {{ number_format($produit->prix, 2) }} €
                                            </small>
                                        </div>
                                        <div class="fw-bold text-danger">
                                            {{ number_format($produit->prix_final, 2) }} €
                                        </div>
                                    @else
                                        <div class="fw-semibold">
                                            {{ number_format($produit->prix, 2) }} €
                                        </div>
                                    @endif
                                </td>
                                <td>
                                    <span class="badge {{ $produit->stock_badge_class }}">
                                        {{ $produit->stock }}
                                    </span>
                                    <div class="small text-muted">
                                        {{ $produit->statut_stock }}
                                    </div>
                                </td>
                                <td>
                                    @if($produit->actif)
                                        <span class="badge bg-success">
                                            <i class="fas fa-check me-1"></i>
                                            Actif
                                        </span>
                                    @else
                                        <span class="badge bg-secondary">
                                            <i class="fas fa-times me-1"></i>
                                            Inactif
                                        </span>
                                    @endif
                                </td>
                                <td class="text-end">
                                    <div class="btn-group btn-group-sm" role="group">
                                        <a href="{{ route('produits.show', $produit) }}" 
                                           class="btn btn-outline-primary"
                                           title="Voir">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <a href="{{ route('produits.edit', $produit) }}" 
                                           class="btn btn-outline-secondary"
                                           title="Modifier">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <button type="button" 
                                                class="btn btn-outline-danger"
                                                onclick="confirmDelete(document.getElementById('delete-form-{{ $produit->id }}'))"
                                                title="Supprimer">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                    
                                    <form id="delete-form-{{ $produit->id }}" 
                                          action="{{ route('produits.destroy', $produit) }}" 
                                          method="POST" 
                                          class="d-none">
                                        @csrf
                                        @method('DELETE')
                                    </form>
                                </td>
                            </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>
            
            <!-- Pagination -->
            <div class="d-flex justify-content-between align-items-center mt-3">
                <div class="text-muted small">
                    Affichage de {{ $produits->firstItem() }} à {{ $produits->lastItem() }} sur {{ $produits->total() }} produits
                </div>
                <div>
                    {{ $produits->links() }}
                </div>
            </div>
        @endif
    </div>
</div>
@endsection