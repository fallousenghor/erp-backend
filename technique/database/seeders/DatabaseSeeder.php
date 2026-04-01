<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;

/**
 * Class DatabaseSeeder
 * 
 * Seeder principal qui orchestre tous les autres seeders.
 * 
 * Utilisation :
 * php artisan db:seed
 * php artisan migrate:fresh --seed (avec migrations)
 */
class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $this->command->info('🌱 Début du seeding de la base de données...');
        $this->command->newLine();

        // Ordre important : Les catégories doivent être créées avant les produits
        // car les produits ont une clé étrangère vers les catégories
        $this->call([
            CategorieSeeder::class,
            ProduitSeeder::class,
        ]);

        $this->command->newLine();
        $this->command->info('✨ Seeding terminé avec succès !');
    }
}