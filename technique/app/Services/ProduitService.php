<?php

namespace App\Services;

use App\Models\Produit;
use App\Repositories\Contracts\ProduitRepositoryInterface;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Http\UploadedFile;
use Illuminate\Pagination\LengthAwarePaginator;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

/**
 * Class ProduitService
 * 
 * Couche service pour la logique métier des produits.
 * Respecte le principe SRP : gère uniquement la logique métier.
 * Utilise le repository pour l'accès aux données (Separation of Concerns).
 */
class ProduitService
{
    /**
     * Injection du repository via le constructeur (Dependency Injection)
     */
    public function __construct(
        protected ProduitRepositoryInterface $repository
    ) {
    }

    /**
     * Liste tous les produits avec pagination et filtres
     */
    public function listerProduits(array $filters = []): LengthAwarePaginator
    {
        return $this->repository->search($filters);
    }

    /**
     * Récupère un produit par ID
     */
    public function trouverProduit(int $id): ?Produit
    {
        return $this->repository->find($id);
    }

    /**
     * Récupère un produit par slug
     */
    public function trouverParSlug(string $slug): ?Produit
    {
        return $this->repository->findBySlug($slug);
    }

    /**
     * Crée un nouveau produit
     * 
     * @throws \Exception
     */
    public function creerProduit(array $data, ?UploadedFile $image = null): Produit
    {
        try {
            DB::beginTransaction();

            // Gestion de l'image si présente
            if ($image) {
                $produitTemp = new Produit();
                $data['image'] = $produitTemp->uploadImage($image, 'produits');
            }

            // Création du produit
            $produit = $this->repository->create($data);

            // Log de l'action
            Log::info('Produit créé', [
                'produit_id' => $produit->id,
                'nom' => $produit->nom,
            ]);

            DB::commit();

            return $produit;
            
        } catch (\Exception $e) {
            DB::rollBack();
            
            // Supprime l'image uploadée en cas d'erreur
            if (isset($data['image'])) {
                $produitTemp = new Produit();
                $produitTemp->deleteOldImage($data['image']);
            }

            Log::error('Erreur création produit', [
                'error' => $e->getMessage(),
                'data' => $data,
            ]);

            throw $e;
        }
    }

    /**
     * Met à jour un produit
     * 
     * @throws \Exception
     */
    public function mettreAJourProduit(int $id, array $data, ?UploadedFile $image = null): Produit
    {
        try {
            DB::beginTransaction();

            $produit = $this->repository->findOrFail($id);
            $ancienneImage = $produit->image;

            // Gestion de la nouvelle image
            if ($image) {
                $data['image'] = $produit->uploadImage($image, 'produits');
                
                // Supprime l'ancienne image
                if ($ancienneImage) {
                    $produit->deleteOldImage($ancienneImage);
                }
            }

            // Mise à jour
            $produit = $this->repository->update($id, $data);

            Log::info('Produit mis à jour', [
                'produit_id' => $produit->id,
                'nom' => $produit->nom,
            ]);

            DB::commit();

            return $produit;
            
        } catch (\Exception $e) {
            DB::rollBack();

            // Supprime la nouvelle image en cas d'erreur
            if (isset($data['image']) && $data['image'] !== $ancienneImage) {
                $produit->deleteOldImage($data['image']);
            }

            Log::error('Erreur mise à jour produit', [
                'produit_id' => $id,
                'error' => $e->getMessage(),
            ]);

            throw $e;
        }
    }

    /**
     * Supprime un produit
     * 
     * @throws \Exception
     */
    public function supprimerProduit(int $id): bool
    {
        try {
            DB::beginTransaction();

            $produit = $this->repository->findOrFail($id);
            $result = $this->repository->delete($id);

            Log::info('Produit supprimé', [
                'produit_id' => $id,
                'nom' => $produit->nom,
            ]);

            DB::commit();

            return $result;
            
        } catch (\Exception $e) {
            DB::rollBack();

            Log::error('Erreur suppression produit', [
                'produit_id' => $id,
                'error' => $e->getMessage(),
            ]);

            throw $e;
        }
    }

    /**
     * Gère le stock d'un produit
     */
    public function gererStock(int $id, int $quantite, string $operation = 'ajouter'): Produit
    {
        $produit = $this->repository->findOrFail($id);

        $result = match($operation) {
            'ajouter' => $produit->ajouterStock($quantite),
            'retirer' => $produit->retirerStock($quantite),
            default => throw new \InvalidArgumentException('Opération invalide'),
        };

        if ($operation === 'retirer' && !$result) {
            throw new \Exception('Stock insuffisant');
        }

        Log::info('Stock mis à jour', [
            'produit_id' => $id,
            'operation' => $operation,
            'quantite' => $quantite,
            'nouveau_stock' => $produit->fresh()->stock,
        ]);

        return $produit->fresh();
    }

    /**
     * Active ou désactive une promotion
     */
    public function gererPromotion(int $id, bool $activer, ?float $prixPromotion = null): Produit
    {
        $produit = $this->repository->findOrFail($id);

        if ($activer) {
            if (!$prixPromotion || $prixPromotion >= $produit->prix) {
                throw new \InvalidArgumentException('Prix de promotion invalide');
            }
            $produit->activerPromotion($prixPromotion);
        } else {
            $produit->desactiverPromotion();
        }

        Log::info('Promotion ' . ($activer ? 'activée' : 'désactivée'), [
            'produit_id' => $id,
            'prix_promotion' => $prixPromotion,
        ]);

        return $produit->fresh();
    }

    /**
     * Récupère les produits en promotion
     */
    public function getProduitsEnPromotion(): Collection
    {
        return $this->repository->getEnPromotion();
    }

    /**
     * Récupère les produits en rupture de stock
     */
    public function getProduitsRuptureStock(): Collection
    {
        return $this->repository->getRuptureStock();
    }

    /**
     * Statistiques des produits
     */
    public function getStatistiques(): array
    {
        return [
            'total' => Produit::count(),
            'actifs' => Produit::actifs()->count(),
            'en_stock' => Produit::enStock()->count(),
            'rupture_stock' => Produit::where('stock', 0)->count(),
            'en_promotion' => Produit::enPromotion()->count(),
            'valeur_stock_totale' => Produit::sum(DB::raw('prix * stock')),
        ];
    }
}