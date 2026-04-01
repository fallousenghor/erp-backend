<?php

namespace App\Repositories;

use App\Models\Produit;
use App\Repositories\Contracts\ProduitRepositoryInterface;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Pagination\LengthAwarePaginator;

/**
 * Class EloquentProduitRepository
 * 
 * Implémentation concrète du repository des produits.
 * Encapsule toute la logique d'accès aux données.
 * Respecte le principe SRP (Single Responsibility) : gère uniquement l'accès aux données.
 */
class EloquentProduitRepository implements ProduitRepositoryInterface
{
    /**
     * Constructeur avec injection du modèle
     */
    public function __construct(protected Produit $model)
    {
    }

    /**
     * Récupère tous les produits avec pagination
     */
    public function paginate(int $perPage = 15): LengthAwarePaginator
    {
        return $this->model
            ->with('categorie')
            ->latest()
            ->paginate($perPage);
    }

    /**
     * Récupère tous les produits
     */
    public function all(): Collection
    {
        return $this->model
            ->with('categorie')
            ->latest()
            ->get();
    }

    /**
     * Trouve un produit par ID
     */
    public function find(int $id): ?Model
    {
        return $this->model
            ->with('categorie')
            ->find($id);
    }

    /**
     * Trouve un produit par ID ou échoue
     */
    public function findOrFail(int $id): Model
    {
        return $this->model
            ->with('categorie')
            ->findOrFail($id);
    }

    /**
     * Trouve un produit par slug
     */
    public function findBySlug(string $slug): ?Model
    {
        return $this->model
            ->with('categorie')
            ->where('slug', $slug)
            ->first();
    }

    /**
     * Crée un nouveau produit
     */
    public function create(array $data): Model
    {
        return $this->model->create($data);
    }

    /**
     * Met à jour un produit
     */
    public function update(int $id, array $data): Model
    {
        $produit = $this->findOrFail($id);
        $produit->update($data);
        
        return $produit->fresh(['categorie']);
    }

    /**
     * Supprime un produit
     */
    public function delete(int $id): bool
    {
        $produit = $this->findOrFail($id);
        return $produit->delete();
    }

    /**
     * Recherche des produits selon des critères
     */
    public function search(array $criteria): LengthAwarePaginator
    {
        $query = $this->model->with('categorie');

        // Recherche par texte
        if (!empty($criteria['search'])) {
            $query->recherche($criteria['search']);
        }

        // Filtre par catégorie
        if (!empty($criteria['categorie_id'])) {
            $query->parCategorie($criteria['categorie_id']);
        }

        // Filtre par statut actif
        if (isset($criteria['actif'])) {
            $query->where('actif', $criteria['actif']);
        }

        // Filtre en stock
        if (!empty($criteria['en_stock'])) {
            $query->enStock();
        }

        // Filtre en promotion
        if (!empty($criteria['en_promotion'])) {
            $query->enPromotion();
        }

        // Tri
        $sort = $criteria['sort'] ?? 'recent';
        $query->trierPar($sort);

        // Pagination
        $perPage = $criteria['per_page'] ?? 15;
        
        return $query->paginate($perPage);
    }

    /**
     * Récupère les produits actifs
     */
    public function getActifs(): Collection
    {
        return $this->model
            ->with('categorie')
            ->actifs()
            ->latest()
            ->get();
    }

    /**
     * Récupère les produits en promotion
     */
    public function getEnPromotion(): Collection
    {
        return $this->model
            ->with('categorie')
            ->actifs()
            ->enPromotion()
            ->latest()
            ->get();
    }

    /**
     * Récupère les produits en rupture de stock
     */
    public function getRuptureStock(): Collection
    {
        return $this->model
            ->with('categorie')
            ->where('stock', 0)
            ->latest()
            ->get();
    }
}