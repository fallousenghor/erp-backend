<?php

namespace Database\Seeders;

use App\Models\Categorie;
use App\Models\Produit;
use Illuminate\Database\Seeder;

/**
 * Class ProduitSeeder
 * 
 * Peuple la base de données avec des produits de test.
 */
class ProduitSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Récupération des catégories
        $electronique = Categorie::where('nom', 'Électronique')->first();
        $informatique = Categorie::where('nom', 'Informatique')->first();
        $mobilier = Categorie::where('nom', 'Mobilier')->first();
        $accessoires = Categorie::where('nom', 'Accessoires')->first();
        $audio = Categorie::where('nom', 'Audio & Vidéo')->first();

        $produits = [
            // Électronique
            [
                'nom' => 'MacBook Pro 16 pouces',
                'description' => 'Ordinateur portable haute performance avec processeur M3 Pro, 16 Go de RAM et 512 Go de SSD. Écran Retina XDR exceptionnel.',
                'prix' => 2799.00,
                'stock' => 15,
                'reference' => 'MBP-16-M3',
                'categorie_id' => $electronique->id,
                'actif' => true,
                'en_promotion' => true,
                'prix_promotion' => 2499.00,
            ],
            [
                'nom' => 'iPhone 15 Pro Max',
                'description' => 'Smartphone haut de gamme avec puce A17 Pro, appareil photo 48MP, écran Super Retina XDR de 6,7 pouces.',
                'prix' => 1479.00,
                'stock' => 25,
                'reference' => 'IP15-PM-256',
                'categorie_id' => $electronique->id,
                'actif' => true,
            ],
            [
                'nom' => 'iPad Pro 12.9',
                'description' => 'Tablette professionnelle avec puce M2, écran Liquid Retina XDR, compatible Apple Pencil et Magic Keyboard.',
                'prix' => 1469.00,
                'stock' => 12,
                'reference' => 'IPAD-PRO-12',
                'categorie_id' => $electronique->id,
                'actif' => true,
                'en_promotion' => true,
                'prix_promotion' => 1299.00,
            ],
            
            // Informatique
            [
                'nom' => 'Dell XPS 15',
                'description' => 'Ordinateur portable premium avec Intel Core i7, 32 Go RAM, 1 To SSD, écran 4K OLED tactile.',
                'prix' => 2299.00,
                'stock' => 8,
                'reference' => 'DELL-XPS15-I7',
                'categorie_id' => $informatique->id,
                'actif' => true,
            ],
            [
                'nom' => 'Logitech MX Master 3S',
                'description' => 'Souris sans fil ergonomique pour professionnels, capteur 8000 DPI, batterie longue durée.',
                'prix' => 109.99,
                'stock' => 45,
                'reference' => 'LOG-MX3S',
                'categorie_id' => $informatique->id,
                'actif' => true,
            ],
            [
                'nom' => 'Samsung 970 EVO Plus SSD 1To',
                'description' => 'SSD NVMe M.2 ultra-rapide, vitesse de lecture jusqu\'à 3500 Mo/s, idéal pour gaming et création.',
                'prix' => 129.00,
                'stock' => 0, // Rupture de stock
                'reference' => 'SSD-970-1TB',
                'categorie_id' => $informatique->id,
                'actif' => true,
            ],
            
            // Mobilier
            [
                'nom' => 'Bureau Ergonomique Réglable',
                'description' => 'Bureau électrique réglable en hauteur de 70 à 120 cm, plateau 140x70 cm, charge max 80kg.',
                'prix' => 499.00,
                'stock' => 5,
                'reference' => 'DESK-ERG-140',
                'categorie_id' => $mobilier->id,
                'actif' => true,
                'en_promotion' => true,
                'prix_promotion' => 399.00,
            ],
            [
                'nom' => 'Chaise Herman Miller Aeron',
                'description' => 'Chaise de bureau ergonomique premium, support lombaire réglable, assise en mesh respirant.',
                'prix' => 1295.00,
                'stock' => 3,
                'reference' => 'CHAIR-HM-AERON',
                'categorie_id' => $mobilier->id,
                'actif' => true,
            ],
            
            // Accessoires
            [
                'nom' => 'Câble USB-C vers Lightning 2m',
                'description' => 'Câble de charge et synchronisation Apple certifié MFi, compatible charge rapide.',
                'prix' => 29.00,
                'stock' => 150,
                'reference' => 'CABLE-USBC-LGT',
                'categorie_id' => $accessoires->id,
                'actif' => true,
            ],
            [
                'nom' => 'Adaptateur USB-C Hub 7-en-1',
                'description' => 'Hub multiport avec HDMI 4K, 3x USB 3.0, lecteur SD/microSD, USB-C PD 100W.',
                'prix' => 49.99,
                'stock' => 67,
                'reference' => 'HUB-7IN1-USBC',
                'categorie_id' => $accessoires->id,
                'actif' => true,
            ],
            
            // Audio & Vidéo
            [
                'nom' => 'Sony WH-1000XM5',
                'description' => 'Casque sans fil à réduction de bruit active, autonomie 30h, audio haute résolution.',
                'prix' => 399.00,
                'stock' => 22,
                'reference' => 'SONY-WH1000XM5',
                'categorie_id' => $audio->id,
                'actif' => true,
                'en_promotion' => true,
                'prix_promotion' => 349.00,
            ],
            [
                'nom' => 'Blue Yeti Microphone USB',
                'description' => 'Microphone USB professionnel pour streaming et podcast, 4 modes de capture.',
                'prix' => 139.00,
                'stock' => 18,
                'reference' => 'BLUE-YETI-USB',
                'categorie_id' => $audio->id,
                'actif' => true,
            ],
            [
                'nom' => 'Logitech C920 Webcam HD Pro',
                'description' => 'Webcam Full HD 1080p pour visioconférence, correction automatique de la lumière.',
                'prix' => 79.99,
                'stock' => 34,
                'reference' => 'LOG-C920-HD',
                'categorie_id' => $audio->id,
                'actif' => true,
            ],
        ];

        foreach ($produits as $produitData) {
            Produit::create($produitData);
        }

        $this->command->info('✅ ' . count($produits) . ' produits créés avec succès.');
        
        // Statistiques
        $total = Produit::count();
        $enPromotion = Produit::where('en_promotion', true)->count();
        $enRupture = Produit::where('stock', 0)->count();
        
        $this->command->info("📊 Statistiques :");
        $this->command->info("   - Total produits : {$total}");
        $this->command->info("   - En promotion : {$enPromotion}");
        $this->command->info("   - En rupture : {$enRupture}");
    }
}