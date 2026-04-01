<?php

use App\Http\Controllers\ProduitController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Routes pour la gestion des produits
| Utilise le pattern RESTful avec des routes resourceful
|
*/

Route::get('/', function () {
    return redirect()->route('produits.index');
});

/*
|--------------------------------------------------------------------------
| Routes des Produits (RESTful)
|--------------------------------------------------------------------------
*/

// Route resourceful pour le CRUD complet
// Génère automatiquement : index, create, store, show, edit, update, destroy
Route::resource('produits', ProduitController::class);

/*
|--------------------------------------------------------------------------
| Routes personnalisées pour les actions spécifiques
|--------------------------------------------------------------------------
*/

// Gestion du stock (ajouter/retirer)
Route::post('produits/{produit}/stock', [ProduitController::class, 'gererStock'])
    ->name('produits.stock');

// Gestion des promotions
Route::post('produits/{produit}/promotion', [ProduitController::class, 'gererPromotion'])
    ->name('produits.promotion');

/*
|--------------------------------------------------------------------------
| Routes générées par Route::resource('produits', ProduitController::class)
|--------------------------------------------------------------------------
|
| Verb      Path                          Action   Route Name
| --------- ----------------------------- -------- -------------------
| GET       /produits                     index    produits.index
| GET       /produits/create              create   produits.create
| POST      /produits                     store    produits.store
| GET       /produits/{produit}           show     produits.show
| GET       /produits/{produit}/edit      edit     produits.edit
| PUT/PATCH /produits/{produit}           update   produits.update
| DELETE    /produits/{produit}           destroy  produits.destroy
|
*/