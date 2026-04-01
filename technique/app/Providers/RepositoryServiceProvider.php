<?php

namespace App\Providers;

use App\Repositories\Contracts\ProduitRepositoryInterface;
use App\Repositories\EloquentProduitRepository;
use Illuminate\Support\ServiceProvider;

/**
 * Class RepositoryServiceProvider
 * 
 * Service Provider pour enregistrer les bindings des repositories.
 * Implémente le principe de Dependency Inversion (SOLID).
 * 
 * Les contrôleurs et services dépendent des interfaces (abstractions),
 * pas des implémentations concrètes.
 */
class RepositoryServiceProvider extends ServiceProvider
{
    /**
     * Register services.
     * 
     * Enregistre les bindings dans le Service Container de Laravel.
     */
    public function register(): void
    {
        // Lie l'interface ProduitRepositoryInterface à son implémentation
        // Quand Laravel résout ProduitRepositoryInterface, il injecte EloquentProduitRepository
        $this->app->bind(
            ProduitRepositoryInterface::class,
            EloquentProduitRepository::class
        );

        // Alternative avec singleton (une seule instance par requête)
        // $this->app->singleton(
        //     ProduitRepositoryInterface::class,
        //     EloquentProduitRepository::class
        // );
    }

    /**
     * Bootstrap services.
     * 
     * Exécuté après que tous les services sont enregistrés.
     */
    public function boot(): void
    {
        //
    }
}