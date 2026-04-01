<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('produits', function (Blueprint $table) {
            $table->id();
            $table->string('nom', 255);
            $table->string('slug', 255)->unique();
            $table->text('description')->nullable();
            $table->decimal('prix', 10, 2);
            $table->integer('stock')->default(0);
            $table->string('image')->nullable();
            $table->string('reference', 50)->unique();
            $table->foreignId('categorie_id')
                  ->constrained('categories')
                  ->onDelete('cascade');
            $table->boolean('actif')->default(true);
            $table->boolean('en_promotion')->default(false);
            $table->decimal('prix_promotion', 10, 2)->nullable();
            $table->timestamps();
            $table->softDeletes();
            
            // Index pour optimisation
            $table->index('nom');
            $table->index('slug');
            $table->index('reference');
            $table->index('categorie_id');
            $table->index('actif');
            $table->index(['actif', 'stock']); // Index composé
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('produits');
    }
};