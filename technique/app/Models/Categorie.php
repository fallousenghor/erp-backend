<?php

namespace App\Models;

use App\Traits\Sluggable;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * Modèle Categorie
 * 
 * Représente une catégorie de produits.
 * Utilise le trait Sluggable pour générer automatiquement le slug.
 */
class Categorie extends Model
{
    use HasFactory, SoftDeletes, Sluggable;

    /**
     * Table associée au modèle
     */
    protected $table = 'categories';

    /**
     * Attributs assignables en masse (protection contre mass assignment)
     */
    protected $fillable = [
        'nom',
        'slug',
        'description',
        'actif',
    ];

    /**
     * Cast des attributs vers des types natifs
     */
    protected $casts = [
        'actif' => 'boolean',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
        'deleted_at' => 'datetime',
    ];

    /**
     * Attributs cachés lors de la sérialisation
     */
    protected $hidden = [
        'deleted_at',
    ];

    /**
     * Relation : Une catégorie a plusieurs produits
     */
    public function produits(): HasMany
    {
        return $this->hasMany(Produit::class, 'categorie_id');
    }

    /**
     * Scope : Filtrer les catégories actives
     * 
     * Utilisation : Categorie::actives()->get()
     */
    public function scopeActives($query)
    {
        return $query->where('actif', true);
    }

    /**
     * Scope : Catégories avec nombre de produits
     */
    public function scopeAvecCompteProduits($query)
    {
        return $query->withCount('produits');
    }

    /**
     * Accesseur : Nom formaté en majuscules
     */
    public function getNomFormateAttribute(): string
    {
        return strtoupper($this->nom);
    }

    /**
     * Mutateur : Normalise le nom avant sauvegarde
     */
    public function setNomAttribute($value): void
    {
        $this->attributes['nom'] = ucfirst(trim($value));
    }

    /**
     * Vérifie si la catégorie a des produits
     */
    public function hasProduits(): bool
    {
        return $this->produits()->exists();
    }
}