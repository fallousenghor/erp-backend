<?php

namespace App\Traits;

use Illuminate\Support\Str;

/**
 * Trait Sluggable
 * 
 * Génère automatiquement un slug unique basé sur un attribut donné.
 * Respecte le principe DRY en évitant la répétition de code.
 */
trait Sluggable
{
    /**
     * Boot du trait - hook dans le cycle de vie Eloquent
     */
    protected static function bootSluggable(): void
    {
        static::creating(function ($model) {
            if (empty($model->slug)) {
                $model->slug = $model->generateUniqueSlug();
            }
        });

        static::updating(function ($model) {
            if ($model->isDirty($model->getSlugSourceColumn())) {
                $model->slug = $model->generateUniqueSlug();
            }
        });
    }

    /**
     * Génère un slug unique
     */
    protected function generateUniqueSlug(): string
    {
        $sourceColumn = $this->getSlugSourceColumn();
        $slug = Str::slug($this->{$sourceColumn});
        $originalSlug = $slug;
        $counter = 1;

        // Vérifie l'unicité et ajoute un suffixe si nécessaire
        while ($this->slugExists($slug)) {
            $slug = $originalSlug . '-' . $counter;
            $counter++;
        }

        return $slug;
    }

    /**
     * Vérifie si le slug existe déjà
     */
    protected function slugExists(string $slug): bool
    {
        $query = static::where('slug', $slug);

        // Exclut le modèle actuel lors d'une mise à jour
        if ($this->exists) {
            $query->where('id', '!=', $this->id);
        }

        return $query->exists();
    }

    /**
     * Retourne le nom de la colonne source pour le slug
     * Par défaut 'nom', peut être surchargé dans le modèle
     */
    protected function getSlugSourceColumn(): string
    {
        return property_exists($this, 'slugSourceColumn') 
            ? $this->slugSourceColumn 
            : 'nom';
    }
}