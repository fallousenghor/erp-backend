<?php

namespace App\Models;

use App\Traits\HasImage;
use App\Traits\Sluggable;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * Modèle Produit
 * 
 * Représente un produit dans le système.
 * Utilise les traits Sluggable et HasImage pour la réutilisation du code (DRY).
 */
class Produit extends Model
{
    use HasFactory, SoftDeletes, Sluggable, HasImage;

    /**
     * Table associée
     */
    protected $table = 'produits';

    /**
     * Active la suppression automatique de l'image
     */
    protected $deleteImageOnDelete = true;

    /**
     * Attributs assignables en masse
     */
    protected $fillable = [
        'nom',
        'slug',
        'description',
        'prix',
        'stock',
        'image',
        'reference',
        'categorie_id',
        'actif',
        'en_promotion',
        'prix_promotion',
    ];

    /**
     * Cast des attributs
     */
    protected $casts = [
        'prix' => 'decimal:2',
        'prix_promotion' => 'decimal:2',
        'stock' => 'integer',
        'actif' => 'boolean',
        'en_promotion' => 'boolean',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
        'deleted_at' => 'datetime',
    ];

    /**
     * Attributs ajoutés lors de la sérialisation
     */
    protected $appends = [
        'prix_final',
        'en_stock',
        'image_url',
    ];

    /**
     * Attributs cachés
     */
    protected $hidden = [
        'deleted_at',
    ];

    /**
     * Relation : Un produit appartient à une catégorie
     */
    public function categorie(): BelongsTo
    {
        return $this->belongsTo(Categorie::class, 'categorie_id');
    }

    /*
    |--------------------------------------------------------------------------
    | SCOPES (Requêtes réutilisables)
    |--------------------------------------------------------------------------
    */

    /**
     * Scope : Produits actifs
     */
    public function scopeActifs($query)
    {
        return $query->where('actif', true);
    }

    /**
     * Scope : Produits en stock
     */
    public function scopeEnStock($query)
    {
        return $query->where('stock', '>', 0);
    }

    /**
     * Scope : Produits en promotion
     */
    public function scopeEnPromotion($query)
    {
        return $query->where('en_promotion', true)
                     ->whereNotNull('prix_promotion');
    }

    /**
     * Scope : Recherche par nom ou référence
     */
    public function scopeRecherche($query, ?string $terme)
    {
        if (empty($terme)) {
            return $query;
        }

        return $query->where(function ($q) use ($terme) {
            $q->where('nom', 'LIKE', "%{$terme}%")
              ->orWhere('reference', 'LIKE', "%{$terme}%")
              ->orWhere('description', 'LIKE', "%{$terme}%");
        });
    }

    /**
     * Scope : Filtrer par catégorie
     */
    public function scopeParCategorie($query, ?int $categorieId)
    {
        if (empty($categorieId)) {
            return $query;
        }

        return $query->where('categorie_id', $categorieId);
    }

    /**
     * Scope : Trier par critère
     */
    public function scopeTrierPar($query, string $critere = 'recent')
    {
        return match($critere) {
            'prix_asc' => $query->orderBy('prix', 'asc'),
            'prix_desc' => $query->orderBy('prix', 'desc'),
            'nom' => $query->orderBy('nom', 'asc'),
            'stock' => $query->orderBy('stock', 'desc'),
            default => $query->latest(), // Par défaut : plus récents
        };
    }

    /*
    |--------------------------------------------------------------------------
    | ACCESSEURS (Getters)
    |--------------------------------------------------------------------------
    */

    /**
     * Accesseur : Prix final (avec promotion si applicable)
     */
    public function getPrixFinalAttribute(): float
    {
        if ($this->en_promotion && $this->prix_promotion) {
            return (float) $this->prix_promotion;
        }

        return (float) $this->prix;
    }

    /**
     * Accesseur : Prix formaté
     */
    public function getPrixFormateAttribute(): string
    {
        return number_format($this->prix_final, 2, ',', ' ') . ' €';
    }

    /**
     * Accesseur : Pourcentage de réduction
     */
    public function getPourcentageReductionAttribute(): ?int
    {
        if (!$this->en_promotion || !$this->prix_promotion) {
            return null;
        }

        $reduction = (($this->prix - $this->prix_promotion) / $this->prix) * 100;
        return (int) round($reduction);
    }

    /**
     * Accesseur : Vérifie si le produit est en stock
     */
    public function getEnStockAttribute(): bool
    {
        return $this->stock > 0;
    }

    /**
     * Accesseur : Statut du stock
     */
    public function getStatutStockAttribute(): string
    {
        return match(true) {
            $this->stock === 0 => 'Rupture de stock',
            $this->stock <= 5 => 'Stock faible',
            default => 'En stock',
        };
    }

    /**
     * Accesseur : Classe CSS pour le badge de stock
     */
    public function getStockBadgeClassAttribute(): string
    {
        return match(true) {
            $this->stock === 0 => 'badge-danger',
            $this->stock <= 5 => 'badge-warning',
            default => 'badge-success',
        };
    }

    /*
    |--------------------------------------------------------------------------
    | MUTATEURS (Setters)
    |--------------------------------------------------------------------------
    */

    /**
     * Mutateur : Normalise le nom
     */
    public function setNomAttribute($value): void
    {
        $this->attributes['nom'] = ucfirst(trim($value));
    }

    /**
     * Mutateur : Génère automatiquement une référence si vide
     */
    public function setReferenceAttribute($value): void
    {
        $this->attributes['reference'] = !empty($value) 
            ? strtoupper(trim($value))
            : 'PROD-' . strtoupper(uniqid());
    }

    /**
     * Mutateur : S'assure que le prix est positif
     */
    public function setPrixAttribute($value): void
    {
        $this->attributes['prix'] = max(0, (float) $value);
    }

    /**
     * Mutateur : S'assure que le stock est positif
     */
    public function setStockAttribute($value): void
    {
        $this->attributes['stock'] = max(0, (int) $value);
    }

    /*
    |--------------------------------------------------------------------------
    | MÉTHODES MÉTIER
    |--------------------------------------------------------------------------
    */

    /**
     * Ajoute du stock
     */
    public function ajouterStock(int $quantite): self
    {
        $this->increment('stock', $quantite);
        return $this;
    }

    /**
     * Retire du stock
     */
    public function retirerStock(int $quantite): bool
    {
        if ($this->stock < $quantite) {
            return false;
        }

        $this->decrement('stock', $quantite);
        return true;
    }

    /**
     * Active une promotion
     */
    public function activerPromotion(float $prixPromotion): self
    {
        $this->update([
            'en_promotion' => true,
            'prix_promotion' => $prixPromotion,
        ]);

        return $this;
    }

    /**
     * Désactive la promotion
     */
    public function desactiverPromotion(): self
    {
        $this->update([
            'en_promotion' => false,
            'prix_promotion' => null,
        ]);

        return $this;
    }

    /**
     * Vérifie si le produit peut être commandé
     */
    public function estCommandable(): bool
    {
        return $this->actif && $this->en_stock;
    }
}