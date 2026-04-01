<?php

namespace App\Repositories\Contracts;

use App\Models\Produit;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Pagination\LengthAwarePaginator;

/**
 * Interface ProduitRepositoryInterface
 * 
 * Contrat définissant les opérations disponibles pour la gestion des produits.
 * Les repositories implémentent ce contrat pour garantir une API cohérente.
 * 
 * Ce contrat respecte le principe Interface Segregation (ISP) de SOLID,
 * en fournissant une interface spécifique aux opérations sur les produits.
 */
interface ProduitRepositoryInterface
{
    /**
     * Récupère tous les produits avec pagination
     */
    public function paginate(int $perPage = 15): LengthAwarePaginator;

    /**
     * Récupère tous les produits
     */
    public function all(): Collection;

    /**
     * Trouve un produit par ID
     */
    public function find(int $id): ?Model;

    /**
     * Trouve un produit par ID ou échoue
     */
    public function findOrFail(int $id): Model;

    /**
     * Trouve un produit par slug
     */
    public function findBySlug(string $slug): ?Model;

    /**
     * Crée un nouveau produit
     */
    public function create(array $data): Model;

    /**
     * Met à jour un produit
     */
    public function update(int $id, array $data): Model;

    /**
     * Supprime un produit
     */
    public function delete(int $id): bool;

    /**
     * Recherche des produits selon des critères
     */
    public function search(array $criteria): LengthAwarePaginator;

    /**
     * Récupère les produits actifs
     */
    public function getActifs(): Collection;

    /**
     * Récupère les produits en promotion
     */
    public function getEnPromotion(): Collection;

    /**
     * Récupère les produits en rupture de stock
     */
    public function getRuptureStock(): Collection;
}
