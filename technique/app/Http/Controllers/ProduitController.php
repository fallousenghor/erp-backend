<?php

namespace App\Http\Controllers;

use App\Http\Requests\StoreProduitRequest;
use App\Http\Requests\UpdateProduitRequest;
use App\Models\Categorie;
use App\Services\ProduitService;
use Illuminate\Http\RedirectResponse;
use Illuminate\View\View;

/**
 * Class ProduitController
 * 
 * Contrôleur pour gérer les opérations CRUD sur les produits.
 * Respecte le principe SRP : uniquement la gestion des requêtes HTTP.
 * Délègue la logique métier au ProduitService.
 */
class ProduitController extends Controller
{
    /**
     * Injection du service via le constructeur
     */
    public function __construct(
        protected ProduitService $produitService
    ) {
    }

    /**
     * Affiche la liste des produits avec filtres et pagination
     * 
     * GET /produits
     */
    public function index(): View
    {
        // Récupération des filtres depuis la requête
        $filters = [
            'search' => request('search'),
            'categorie_id' => request('categorie_id'),
            'actif' => request('actif'),
            'en_stock' => request('en_stock'),
            'en_promotion' => request('en_promotion'),
            'sort' => request('sort', 'recent'),
            'per_page' => request('per_page', 15),
        ];

        // Récupération des produits via le service
        $produits = $this->produitService->listerProduits($filters);

        // Récupération des catégories pour le filtre
        $categories = Categorie::actives()->orderBy('nom')->get();

        // Statistiques
        $statistiques = $this->produitService->getStatistiques();

        return view('produits.index', compact('produits', 'categories', 'statistiques', 'filters'));
    }

    /**
     * Affiche le formulaire de création
     * 
     * GET /produits/create
     */
    public function create(): View
    {
        $categories = Categorie::actives()->orderBy('nom')->get();
        
        return view('produits.create', compact('categories'));
    }

    /**
     * Enregistre un nouveau produit
     * 
     * POST /produits
     */
    public function store(StoreProduitRequest $request): RedirectResponse
    {
        try {
            // Les données sont déjà validées par le FormRequest
            $data = $request->validated();
            
            // Récupération de l'image uploadée
            $image = $request->file('image');

            // Création via le service
            $produit = $this->produitService->creerProduit($data, $image);

            return redirect()
                ->route('produits.show', $produit)
                ->with('success', 'Le produit a été créé avec succès.');
                
        } catch (\Exception $e) {
            return redirect()
                ->back()
                ->withInput()
                ->with('error', 'Une erreur est survenue lors de la création du produit.');
        }
    }

    /**
     * Affiche les détails d'un produit
     * 
     * GET /produits/{id}
     */
    public function show(int $id): View
    {
        $produit = $this->produitService->trouverProduit($id);

        if (!$produit) {
            abort(404, 'Produit non trouvé');
        }

        return view('produits.show', compact('produit'));
    }

    /**
     * Affiche le formulaire d'édition
     * 
     * GET /produits/{id}/edit
     */
    public function edit(int $id): View
    {
        $produit = $this->produitService->trouverProduit($id);

        if (!$produit) {
            abort(404, 'Produit non trouvé');
        }

        $categories = Categorie::actives()->orderBy('nom')->get();

        return view('produits.edit', compact('produit', 'categories'));
    }

    /**
     * Met à jour un produit
     * 
     * PUT/PATCH /produits/{id}
     */
    public function update(UpdateProduitRequest $request, int $id): RedirectResponse
    {
        try {
            $data = $request->validated();
            $image = $request->file('image');

            $produit = $this->produitService->mettreAJourProduit($id, $data, $image);

            return redirect()
                ->route('produits.show', $produit)
                ->with('success', 'Le produit a été mis à jour avec succès.');
                
        } catch (\Exception $e) {
            return redirect()
                ->back()
                ->withInput()
                ->with('error', 'Une erreur est survenue lors de la mise à jour.');
        }
    }

    /**
     * Supprime un produit
     * 
     * DELETE /produits/{id}
     */
    public function destroy(int $id): RedirectResponse
    {
        try {
            $this->produitService->supprimerProduit($id);

            return redirect()
                ->route('produits.index')
                ->with('success', 'Le produit a été supprimé avec succès.');
                
        } catch (\Exception $e) {
            return redirect()
                ->back()
                ->with('error', 'Une erreur est survenue lors de la suppression.');
        }
    }

    /**
     * Gère le stock d'un produit (ajouter/retirer)
     * 
     * POST /produits/{id}/stock
     */
    public function gererStock(int $id): RedirectResponse
    {
        try {
            $quantite = (int) request('quantite');
            $operation = request('operation', 'ajouter');

            $this->produitService->gererStock($id, $quantite, $operation);

            return redirect()
                ->back()
                ->with('success', 'Le stock a été mis à jour avec succès.');
                
        } catch (\Exception $e) {
            return redirect()
                ->back()
                ->with('error', $e->getMessage());
        }
    }

    /**
     * Active/désactive une promotion
     * 
     * POST /produits/{id}/promotion
     */
    public function gererPromotion(int $id): RedirectResponse
    {
        try {
            $activer = request()->boolean('activer');
            $prixPromotion = $activer ? (float) request('prix_promotion') : null;

            $this->produitService->gererPromotion($id, $activer, $prixPromotion);

            $message = $activer 
                ? 'La promotion a été activée avec succès.' 
                : 'La promotion a été désactivée avec succès.';

            return redirect()
                ->back()
                ->with('success', $message);
                
        } catch (\Exception $e) {
            return redirect()
                ->back()
                ->with('error', $e->getMessage());
        }
    }
}