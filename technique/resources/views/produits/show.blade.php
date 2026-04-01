@extends('layouts.app')

@section('title', $produit->nom)

@section('content')
<div class="mb-4">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a href="{{ route('produits.index') }}">Produits</a>
            </li>
            <li class="breadcrumb-item active">{{ $produit->nom }}</li>
        </ol>
    </nav>
</div>

<div class="row">
    <div class="col-lg-8">
        <!-- Carte principale du produit -->
        <div class="card mb-4">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <div>
                        <h1 class="h3 mb-2">{{ $produit->nom }}</h1>
                        <div class="d-flex gap-2 align-items-center">
                            <span class="badge bg-secondary">{{ $produit->categorie->nom }}</span>
                            <span class="badge {{ $produit->stock_badge_class }}">
                                {{ $produit->statut_stock }}
                            </span>
                            @if($produit->actif)
                                <span class="badge bg-success">
                                    <i class="fas fa-check me-1"></i> Actif
                                </span>
                            @else
                                <span class="badge bg-secondary">
                                    <i class="fas fa-times me-1"></i> Inactif
                                </span>
                            @endif
                            @if($produit->en_promotion)
                                <span class="badge bg-warning text-dark">
                                    <i class="fas fa-percent me-1"></i>
                                    -{{ $produit->pourcentage_reduction }}%
                                </span>
                            @endif
                        </div>
                    </div>
                    
                    <div class="btn-group">
                        <a href="{{ route('produits.edit', $produit) }}" class="btn btn-primary">
                            <i class="fas fa-edit me-2"></i>
                            Modifier
                        </a>
                        <button type="button" 
                                class="btn btn-danger"
                                onclick="confirmDelete(document.getElementById('delete-form'))">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-5">
                        @if($produit->image)
                            <img src="{{ asset('storage/' . $produit->image) }}" 
                                 alt="{{ $produit->nom }}"
                                 class="img-fluid rounded shadow-sm mb-3">
                        @else
                            <div class="bg-light rounded d-flex align-items-center justify-content-center" 
                                 style="height: 300px;">
                                <div class="text-center text-muted">
                                    <i class="fas fa-image fa-4x mb-3"></i>
                                    <p class="mb-0">Aucune image</p>
                                </div>
                            </div>
                        @endif
                    </div>
                    
                    <div class="col-md-7">
                        <div class="mb-4">
                            <h5 class="text-muted mb-2">Prix</h5>
                            @if($produit->en_promotion)
                                <div class="d-flex align-items-baseline gap-3">
                                    <h2 class="text-danger mb-0">
                                        {{ number_format($produit->prix_final, 2) }} €
                                    </h2>
                                    <span class="text-muted text-decoration-line-through h5">
                                        {{ number_format($produit->prix, 2) }} €
                                    </span>
                                </div>
                                <div class="alert alert-warning mt-2 mb-0">
                                    <i class="fas fa-tag me-2"></i>
                                    <strong>Promotion active !</strong>
                                    Économisez {{ number_format($produit->prix - $produit->prix_final, 2) }} €
                                    ({{ $produit->pourcentage_reduction }}%)
                                </div>
                            @else
                                <h2 class="mb-0">{{ number_format($produit->prix, 2) }} €</h2>
                            @endif
                        </div>
                        
                        <div class="mb-4">
                            <h5 class="text-muted mb-2">Référence</h5>
                            <code class="fs-5">{{ $produit->reference }}</code>
                        </div>
                        
                        <div class="mb-4">
                            <h5 class="text-muted mb-2">Stock disponible</h5>
                            <div class="d-flex align-items-center gap-3">
                                <h3 class="mb-0">{{ $produit->stock }}</h3>
                                <span class="text-muted">unité(s)</span>
                            </div>
                            <div class="progress mt-2" style="height: 10px;">
                                @php
                                    $stockPercentage = min(100, ($produit->stock / 100) * 100);
                                    $progressClass = $produit->stock > 50 ? 'bg-success' : ($produit->stock > 10 ? 'bg-warning' : 'bg-danger');
                                @endphp
                                <div class="progress-bar {{ $progressClass }}" 
                                     role="progressbar" 
                                     style="width: {{ $stockPercentage }}%">
                                </div>
                            </div>
                        </div>
                        
                        @if($produit->description)
                            <div>
                                <h5 class="text-muted mb-2">Description</h5>
                                <p class="text-muted">{{ $produit->description }}</p>
                            </div>
                        @endif
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Actions rapides -->
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="fas fa-tools me-2"></i>
                    Actions rapides
                </h5>
            </div>
            <div class="card-body">
                <div class="row g-3">
                    <!-- Gestion du stock -->
                    <div class="col-md-6">
                        <div class="border rounded p-3">
                            <h6 class="mb-3">
                                <i class="fas fa-boxes me-2"></i>
                                Gestion du stock
                            </h6>
                            <form action="{{ route('produits.stock', $produit) }}" method="POST">
                                @csrf
                                <div class="mb-3">
                                    <label for="quantite" class="form-label">Quantité</label>
                                    <input type="number" 
                                           class="form-control" 
                                           id="quantite" 
                                           name="quantite" 
                                           min="1"
                                           value="1"
                                           required>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" name="operation" value="ajouter" class="btn btn-success">
                                        <i class="fas fa-plus me-2"></i>
                                        Ajouter au stock
                                    </button>
                                    <button type="submit" name="operation" value="retirer" class="btn btn-warning">
                                        <i class="fas fa-minus me-2"></i>
                                        Retirer du stock
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    
                    <!-- Gestion de la promotion -->
                    <div class="col-md-6">
                        <div class="border rounded p-3">
                            <h6 class="mb-3">
                                <i class="fas fa-percent me-2"></i>
                                Gestion de la promotion
                            </h6>
                            
                            @if($produit->en_promotion)
                                <div class="alert alert-success mb-3">
                                    <strong>Promotion active</strong><br>
                                    Prix promotionnel : {{ number_format($produit->prix_promotion, 2) }} €<br>
                                    Réduction : {{ $produit->pourcentage_reduction }}%
                                </div>
                                <form action="{{ route('produits.promotion', $produit) }}" method="POST">
                                    @csrf
                                    <input type="hidden" name="activer" value="0">
                                    <button type="submit" class="btn btn-danger w-100">
                                        <i class="fas fa-times me-2"></i>
                                        Désactiver la promotion
                                    </button>
                                </form>
                            @else
                                <form action="{{ route('produits.promotion', $produit) }}" method="POST">
                                    @csrf
                                    <input type="hidden" name="activer" value="1">
                                    <div class="mb-3">
                                        <label for="prix_promotion" class="form-label">
                                            Prix promotionnel
                                        </label>
                                        <div class="input-group">
                                            <input type="number" 
                                                   class="form-control" 
                                                   id="prix_promotion" 
                                                   name="prix_promotion" 
                                                   step="0.01"
                                                   max="{{ $produit->prix - 0.01 }}"
                                                   placeholder="Prix réduit"
                                                   required>
                                            <span class="input-group-text">€</span>
                                        </div>
                                        <small class="form-text text-muted">
                                            Maximum : {{ number_format($produit->prix - 0.01, 2) }} €
                                        </small>
                                    </div>
                                    <button type="submit" class="btn btn-warning w-100">
                                        <i class="fas fa-check me-2"></i>
                                        Activer la promotion
                                    </button>
                                </form>
                            @endif
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Colonne de droite - Informations -->
    <div class="col-lg-4">
        <!-- Statistiques -->
        <div class="card mb-3">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="fas fa-chart-line me-2"></i>
                    Statistiques
                </h5>
            </div>
            <div class="card-body">
                <div class="mb-3 pb-3 border-bottom">
                    <small class="text-muted d-block mb-1">Valeur du stock</small>
                    <h4 class="mb-0 text-primary">
                        {{ number_format($produit->prix * $produit->stock, 2) }} €
                    </h4>
                </div>
                
                <div class="mb-3 pb-3 border-bottom">
                    <small class="text-muted d-block mb-1">Prix unitaire moyen</small>
                    <h4 class="mb-0">
                        {{ number_format($produit->prix_final, 2) }} €
                    </h4>
                </div>
                
                <div>
                    <small class="text-muted d-block mb-1">Disponibilité</small>
                    @if($produit->estCommandable())
                        <span class="badge bg-success">
                            <i class="fas fa-check-circle me-1"></i>
                            Commandable
                        </span>
                    @else
                        <span class="badge bg-danger">
                            <i class="fas fa-times-circle me-1"></i>
                            Non commandable
                        </span>
                    @endif
                </div>
            </div>
        </div>
        
        <!-- Informations techniques -->
        <div class="card mb-3">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="fas fa-info-circle me-2"></i>
                    Informations techniques
                </h5>
            </div>
            <div class="card-body">
                <div class="mb-3">
                    <small class="text-muted d-block">ID</small>
                    <strong>#{{ $produit->id }}</strong>
                </div>
                
                <div class="mb-3">
                    <small class="text-muted d-block">Slug</small>
                    <code>{{ $produit->slug }}</code>
                </div>
                
                <div class="mb-3">
                    <small class="text-muted d-block">Créé le</small>
                    <strong>{{ $produit->created_at->format('d/m/Y à H:i') }}</strong>
                    <br>
                    <small class="text-muted">
                        {{ $produit->created_at->diffForHumans() }}
                    </small>
                </div>
                
                <div>
                    <small class="text-muted d-block">Dernière modification</small>
                    <strong>{{ $produit->updated_at->format('d/m/Y à H:i') }}</strong>
                    <br>
                    <small class="text-muted">
                        {{ $produit->updated_at->diffForHumans() }}
                    </small>
                </div>
            </div>
        </div>
        
        <!-- Actions supplémentaires -->
        <div class="card">
            <div class="card-body">
                <div class="d-grid gap-2">
                    <a href="{{ route('produits.edit', $produit) }}" class="btn btn-outline-primary">
                        <i class="fas fa-edit me-2"></i>
                        Modifier le produit
                    </a>
                    
                    <button type="button" class="btn btn-outline-secondary" onclick="window.print()">
                        <i class="fas fa-print me-2"></i>
                        Imprimer la fiche
                    </button>
                    
                    <a href="{{ route('produits.index') }}" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-2"></i>
                        Retour à la liste
                    </a>
                    
                    <hr>
                    
                    <button type="button" 
                            class="btn btn-outline-danger"
                            onclick="confirmDelete(document.getElementById('delete-form'))">
                        <i class="fas fa-trash me-2"></i>
                        Supprimer le produit
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Formulaire de suppression -->
<form id="delete-form" 
      action="{{ route('produits.destroy', $produit) }}" 
      method="POST" 
      class="d-none">
    @csrf
    @method('DELETE')
</form>
@endsection

@push('styles')
<style>
    @media print {
        .sidebar, .navbar, .btn, form, .no-print {
            display: none !important;
        }
        .card {
            border: 1px solid #ddd !important;
            box-shadow: none !important;
        }
    }
</style>
@endpush