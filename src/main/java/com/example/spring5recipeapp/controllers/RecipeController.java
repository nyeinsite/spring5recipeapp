package com.example.spring5recipeapp.controllers;

import com.example.spring5recipeapp.domain.Recipe;
import com.example.spring5recipeapp.exceptions.NotFoundException;
import com.example.spring5recipeapp.services.RecipeService;
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
public class RecipeController {
    private static final String RECIPE_RECIPEFROM_URL="recipe/recipeform";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }
    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.findById(new Long(id)));
        return "recipe/show";
    }
    @GetMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe",new Recipe());
        return "recipe/recipeform";
    }
    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model, HttpServletRequest request){
        model.addAttribute("recipe",recipeService.findById(Long.valueOf(id)));
        request.getSession().setAttribute("updateId", id);
        return RECIPE_RECIPEFROM_URL;
    }
    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe")Recipe recipe, BindingResult bindingResult, HttpServletRequest request){
      if(bindingResult.hasErrors()){
          bindingResult.getAllErrors().forEach(objectError -> {
              log.debug(objectError.toString());
          });
          return RECIPE_RECIPEFROM_URL;
      }
     //recipeService.deleteById(Long.valueOf((String) request.getSession().getAttribute("updateId")));
      Recipe savedRecipe=recipeService.saveRecipe(recipe);
      return "redirect:/recipe/"+savedRecipe.getId()+"/show";
    }
 @GetMapping("recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        log.debug("Deleting id: "+id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
    @ResponseStatus(HttpStatus.NOT_FOUND   )
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
