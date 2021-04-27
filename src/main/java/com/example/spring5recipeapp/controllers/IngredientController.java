package com.example.spring5recipeapp.controllers;

import com.example.spring5recipeapp.domain.Ingredient;
import com.example.spring5recipeapp.domain.Recipe;
import com.example.spring5recipeapp.domain.UnitOfMeasure;
import com.example.spring5recipeapp.exceptions.NotFoundException;
import com.example.spring5recipeapp.services.IngredientService;
import com.example.spring5recipeapp.services.RecipeService;
import com.example.spring5recipeapp.services.UnitOfMeasureService;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
public class IngredientController {
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;
    private static final String INGREDIENT_INGREDIENTFORM_URL="recipe/ingredient/ingredientform";
    public IngredientController(IngredientService ingredientService, RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipe id: "+recipeId);
        model.addAttribute("recipe",recipeService.findById(Long.valueOf(recipeId)));
        return  "recipe/ingredient/list";
   }

   @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
   public String showRecipeIngredient(@PathVariable String recipeId,@PathVariable String id,Model model){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),Long.valueOf(id)));
        return "recipe/ingredient/show";
   }
   @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model, HttpServletRequest request){
       request.getSession().setAttribute("recipeId",recipeId);
       Recipe recipe=recipeService.findById(Long.valueOf(recipeId));
       Ingredient ingredient=new Ingredient();
       ingredient.setRecipe(recipe);
       model.addAttribute("ingredient",ingredient);
       ingredient.setUom(new UnitOfMeasure());
       model.addAttribute("uomList",unitOfMeasureService.listAllUoms());
       return "recipe/ingredient/ingredientform";
   }
   @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model, HttpServletRequest request){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),Long.valueOf(id)));
       model.addAttribute("uomList",unitOfMeasureService.listAllUoms());
       request.getSession().setAttribute("recipeId",recipeId);
       return "recipe/ingredient/ingredientform";
   }
   @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@Valid @ModelAttribute("ingredient")Ingredient ingredient, BindingResult bindingResult,HttpServletRequest request){
     if(bindingResult.hasErrors()){
         bindingResult.getAllErrors().forEach(objectError -> {
             log.debug(objectError.toString());
         });
         return INGREDIENT_INGREDIENTFORM_URL;
     }
        Recipe recipe=new Recipe();
        String recipeId=request.getSession().getAttribute("recipeId").toString();
        recipe.setId(Long.valueOf(recipeId));
        ingredient.setRecipe(recipe);
        Ingredient saveIngredient=ingredientService.saveIngredient(ingredient);
//        log.debug("saved recipe id:"+saveIngredient.getRecipe().getId());
        log.debug("saved ingredient id:"+saveIngredient.getId());
        return "redirect:/recipe/"+request.getSession().getAttribute("recipeId")+"/ingredient/"+saveIngredient.getId()+"/show";
   }
   @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId,@PathVariable String id){
        log.debug("deleting ingredient id:"+id);
        ingredientService.deleteById(Long.valueOf(recipeId),Long.valueOf(id));
        return "redirect:/recipe/"+recipeId+"/ingredients";
   }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception){
        log.error("handling not found exception");
        log.error(exception.getMessage());
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception",exception);

        return modelAndView;
    }
}
