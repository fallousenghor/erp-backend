<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

/**
 * Class UpdateProduitRequest
 * 
 * Gère la validation pour la mise à jour d'un produit.
 * Similaire à StoreProduitRequest mais avec des règles d'unicité adaptées.
 */
class UpdateProduitRequest extends FormRequest
{
    /**
     * Détermine si l'utilisateur est autorisé
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Règles de validation
     */
    public function rules(): array
    {
        $produitId = $this->route('produit'); // ID du produit depuis la route

        return [
            'nom' => [
                'required',
                'string',
                'max:255',
                Rule::unique('produits', 'nom')->ignore($produitId),
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
                Rule::unique('produits', 'reference')->ignore($produitId),
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
                'max:2048',
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
                'lt:prix',
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
     * Prépare les données pour la validation
     */
    protected function prepareForValidation(): void
    {
        $this->merge([
            'actif' => $this->boolean('actif', true),
            'en_promotion' => $this->boolean('en_promotion', false),
        ]);
    }

    /**
     * Validation personnalisée
     */
    public function withValidator($validator): void
    {
        $validator->after(function ($validator) {
            if ($this->en_promotion && $this->prix_promotion >= $this->prix) {
                $validator->errors()->add(
                    'prix_promotion',
                    'Le prix promotionnel doit être inférieur au prix normal.'
                );
            }
        });
    }
}