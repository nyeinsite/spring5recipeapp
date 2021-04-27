package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Recipe;

import java.util.ArrayList;
import java.util.Set;

public interface RecipeService {
    ArrayList<Recipe> getRecipes();
    Recipe findById(Long l);
    Recipe saveRecipe(Recipe recipe);
    void deleteById(Long idToDelete);
}
