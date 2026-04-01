<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

/**
 * Class StoreProduitRequest
 * 
 * Gère la validation pour la création d'un produit.
 * Respecte le principe SRP : uniquement la validation.
 */
class StoreProduitRequest extends FormRequest
{
    /**
     * Détermine si l'utilisateur est autorisé à faire cette requête
     */
    public function authorize(): bool
    {
        // Ici vous pouvez ajouter une logique d'autorisation
        // Par exemple : return auth()->check();
        return true;
    }

    /**
     * Règles de validation
     */
    public function rules(): array
    {
        return [
            'nom' => [
                'required',
                'string',
                'max:255',
                'unique:produits,nom',
            ],
            'description' => [
                'nullable',
                'string',
                'max:2000',
            ],
            'prix' => [
                'required',
                'numeric',
                'min:0',
                'max:999999.99',
            ],
            'stock' => [
                'required',
                'integer',
                'min:0',
                'max:999999',
            ],
            'reference' => [
                'nullable',
                'string',
                'max:50',
                'unique:produits,reference',
            ],
            'categorie_id' => [
                'required',
                'integer',
                'exists:categories,id',
            ],
            'image' => [
                'nullable',
                'image',
                'mimes:jpeg,jpg,png,gif,webp',
                'max:2048', // 2MB max
            ],
            'actif' => [
                'nullable',
                'boolean',
            ],
            'en_promotion' => [
                'nullable',
                'boolean',
            ],
            'prix_promotion' => [
                'nullable',
                'required_if:en_promotion,true',
                'numeric',
                'min:0',
                'lt:prix', // Doit être inférieur au prix normal
            ],
        ];
    }

    /**
     * Messages d'erreur personnalisés
     */
    public function messages(): array
    {
        return [
            'nom.required' => 'Le nom du produit est obligatoire.',
            'nom.max' => 'Le nom ne peut pas dépasser 255 caractères.',
            'nom.unique' => 'Ce nom de produit existe déjà.',
            
            'prix.required' => 'Le prix est obligatoire.',
            'prix.numeric' => 'Le prix doit être un nombre.',
            'prix.min' => 'Le prix doit être positif.',
            
            'stock.required' => 'La quantité en stock est obligatoire.',
            'stock.integer' => 'Le stock doit être un nombre entier.',
            'stock.min' => 'Le stock ne peut pas être négatif.',
            
            'reference.unique' => 'Cette référence existe déjà.',
            
            'categorie_id.required' => 'Veuillez sélectionner une catégorie.',
            'categorie_id.exists' => 'La catégorie sélectionnée n\'existe pas.',
            
            'image.image' => 'Le fichier doit être une image.',
            'image.mimes' => 'L\'image doit être au format : jpeg, jpg, png, gif ou webp.',
            'image.max' => 'L\'image ne peut pas dépasser 2 Mo.',
            
            'prix_promotion.required_if' => 'Le prix promotionnel est requis si le produit est en promotion.',
            'prix_promotion.lt' => 'Le prix promotionnel doit être inférieur au prix normal.',
        ];
    }

    /**
     * Noms d'attributs personnalisés
     */
    public function attributes(): array
    {
        return [
            'nom' => 'nom du produit',
            'description' => 'description',
            'prix' => 'prix',
            'stock' => 'stock',
            'reference' => 'référence',
            'categorie_id' => 'catégorie',
            'image' => 'image',
            'prix_promotion' => 'prix promotionnel',
        ];
    }

    /**
     * Prépare les données pour la validation
     */
    protected function prepareForValidation(): void
    {
        // Conversion des valeurs string en boolean
        $this->merge([
            'actif' => $this->boolean('actif', true),
            'en_promotion' => $this->boolean('en_promotion', false),
        ]);
    }

    /**
     * Configure le validateur après sa création
     */
    public function withValidator($validator): void
    {
        $validator->after(function ($validator) {
            // Validation personnalisée supplémentaire
            if ($this->en_promotion && $this->prix_promotion >= $this->prix) {
                $validator->errors()->add(
                    'prix_promotion',
                    'Le prix promotionnel doit être inférieur au prix normal.'
                );
            }
        });
    }
}