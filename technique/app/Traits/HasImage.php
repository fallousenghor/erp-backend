<?php

namespace App\Traits;

use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Storage;

/**
 * Trait HasImage
 * 
 * Gère l'upload, la suppression et l'affichage des images.
 * Principe DRY : évite la duplication du code de gestion d'images.
 */
trait HasImage
{
    /**
     * Upload une image et retourne le chemin
     */
    public function uploadImage(UploadedFile $image, string $directory = 'images'): string
    {
        // Génère un nom unique pour l'image
        $filename = time() . '_' . uniqid() . '.' . $image->getClientOriginalExtension();
        
        // Stocke l'image dans le storage public
        $path = $image->storeAs($directory, $filename, 'public');
        
        return $path;
    }

    /**
     * Supprime l'ancienne image si elle existe
     */
    public function deleteOldImage(?string $imagePath): void
    {
        if ($imagePath && Storage::disk('public')->exists($imagePath)) {
            Storage::disk('public')->delete($imagePath);
        }
    }

    /**
     * Retourne l'URL complète de l'image
     */
    public function getImageUrlAttribute(): ?string
    {
        return $this->image 
            ? Storage::url($this->image)
            : null;
    }

    /**
     * Retourne l'URL de l'image ou une image par défaut
     */
    public function getImageOrDefaultAttribute(): string
    {
        return $this->image_url ?? asset('images/default-product.png');
    }

    /**
     * Hook de suppression pour nettoyer les images
     */
    protected static function bootHasImage(): void
    {
        static::deleting(function ($model) {
            if (property_exists($model, 'deleteImageOnDelete') && $model->deleteImageOnDelete) {
                $model->deleteOldImage($model->image);
            }
        });
    }
}