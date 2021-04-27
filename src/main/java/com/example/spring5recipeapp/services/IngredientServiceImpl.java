package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Ingredient;
import com.example.spring5recipeapp.domain.Recipe;
import com.example.spring5recipeapp.repositories.IngredientRepository;
import com.example.spring5recipeapp.repositories.RecipeRepository;
import com.example.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
private final IngredientRepository ingredientRepository;
    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public Ingredient findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);
        if(!recipeOptional.isPresent()){
            log.error("recipe id not found.Id: "+recipeId);
        }
        Recipe recipe=recipeOptional.get();
Ingredient ingredient=new Ingredient();
        List<Ingredient> ingredients=recipe.getIngredients();
      for(int i=0;i<ingredients.size();i++){
          if(ingredients.get(i).getId().equals(ingredientId)){
          ingredient=ingredients.get(i);
          }
      }
        return ingredient;
    }

    @Override
    @Transactional
    public Ingredient saveIngredient(Ingredient ingredient) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredient.getRecipe().getId());

        if(!recipeOptional.isPresent()){

            //todo toss error if not found!
            log.error("Recipe not found for id: " + ingredient.getRecipe().getId());
            return new Ingredient();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient1 -> ingredient1.getId().equals(ingredient.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredient.getDescription());
                ingredientFound.setAmount(ingredient.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                        .findById(ingredient.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient

                Ingredient ingredient2 = ingredient;
                ingredient2.setRecipe(recipe);
                recipe.getIngredients().add(ingredient2);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(ingredient.getId()))
                    .findFirst();

            //check by description
            if(!savedIngredientOptional.isPresent()){
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredient.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredient.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(ingredient.getUom().getId()))
                        .findFirst();
            }

            //to do check for fail
            return savedIngredientOptional.get();
        }
    }

    @Override
    @Transactional
    public void deleteById(Long recipeId, Long idToDelete) {

log.debug("Deleting ingredient: "+recipeId+":"+idToDelete);
Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);
if(recipeOptional.isPresent()){
    Recipe recipe=recipeOptional.get();
    log.debug("found recipe");
    Optional<Ingredient>ingredientOptional=recipe
            .getIngredients()
            .stream()
            .filter(ingredient -> ingredient.getId().equals(idToDelete))
            .findFirst();
    if(ingredientOptional.isPresent()){
        log.debug("found Ingredient");
        Ingredient ingredientToDelete=ingredientOptional.get();
        ingredientToDelete.setRecipe(null);
        recipe.getIngredients().remove(ingredientOptional.get());
        recipeRepository.save(recipe);
    }
}else {
    log.debug("Recipe Id Not found.Id:"+recipeId);

}
    }
}
