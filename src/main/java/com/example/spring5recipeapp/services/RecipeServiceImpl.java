package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Recipe;
import com.example.spring5recipeapp.exceptions.NotFoundException;
import com.example.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public ArrayList<Recipe> getRecipes() {
        log.debug("I'm in the service");
        List<Recipe> recipeList=new ArrayList<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeList::add);
        return (ArrayList<Recipe>) recipeList;
    }

    @Override
    public Recipe findById(Long l) {
        Optional<Recipe> recipeOptional=recipeRepository.findById(l);
        if(!recipeOptional.isPresent()){
            throw new NotFoundException("Recipe Not Found!.For Id value:"+l.toString());
        }

        return recipeOptional.get();
    }

    @Override
    @Transactional
    public Recipe saveRecipe(Recipe recipe) {
        Recipe savedRecipe=recipeRepository.save(recipe);
        log.debug("Saved RecipeId: "+savedRecipe.getId());
        return savedRecipe;
    }

    @Override
    public void deleteById(Long idToDelete) {
recipeRepository.deleteById(idToDelete);
    }
}
