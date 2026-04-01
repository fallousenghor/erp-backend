<?php

namespace Database\Seeders;

use App\Models\Categorie;
use Illuminate\Database\Seeder;

/**
 * Class CategorieSeeder
 * 
 * Peuple la base de données avec des catégories de test.
 */
class CategorieSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $categories = [
            [
                'nom' => 'Électronique',
                'description' => 'Ordinateurs, téléphones, tablettes et accessoires électroniques',
                'actif' => true,
            ],
            [
                'nom' => 'Informatique',
                'description' => 'Matériel informatique, périphériques et composants',
                'actif' => true,
            ],
            [
                'nom' => 'Mobilier',
                'description' => 'Meubles de bureau et accessoires d\'aménagement',
                'actif' => true,
            ],
            [
                'nom' => 'Accessoires',
                'description' => 'Câbles, adaptateurs, housses et autres accessoires',
                'actif' => true,
            ],
            [
                'nom' => 'Audio & Vidéo',
                'description' => 'Casques, enceintes, microphones et équipements audiovisuels',
                'actif' => true,
            ],
        ];

        foreach ($categories as $categorieData) {
            Categorie::create($categorieData);
        }

        $this->command->info('✅ ' . count($categories) . ' catégories créées avec succès.');
    }
}