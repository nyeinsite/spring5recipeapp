package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Ingredient;

public interface IngredientService {
    Ingredient findByRecipeIdAndIngredientId(Long recipeId,Long ingredientId);
    Ingredient saveIngredient(Ingredient ingredient);
    void deleteById(Long recipeId,Long idToDelete);
}
