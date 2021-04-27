package com.example.spring5recipeapp.bootstrap;

import com.example.spring5recipeapp.domain.*;
import com.example.spring5recipeapp.repositories.CategoryRepository;
import com.example.spring5recipeapp.repositories.RecipeRepository;
import com.example.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }
    private List<Recipe> getRecipes(){
        List<Recipe> recipes=new ArrayList<>(2);
        Optional<UnitOfMeasure> eachUomOptional=unitOfMeasureRepository.findByDescription("Each");
        if(!eachUomOptional.isPresent()){
         //   throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure>tableSpoonUomOptional=unitOfMeasureRepository.findByDescription("Tablespoon");
        if(!tableSpoonUomOptional.isPresent()){
          //  throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure>teaSpoonUomOptional=unitOfMeasureRepository.findByDescription("Teaspoon");
        if(!teaSpoonUomOptional.isPresent()){
         //   throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure>dashUomOptional=unitOfMeasureRepository.findByDescription("Dash");
        if(!dashUomOptional.isPresent()){
          //  throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure>pintUomOptional=unitOfMeasureRepository.findByDescription("Pint");
        if(!pintUomOptional.isPresent()){
           // throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure>cupsUomOptional=unitOfMeasureRepository.findByDescription("Cup");
        if(!cupsUomOptional.isPresent()){
           // throw new RuntimeException("Expected UOM Not Found");
        }
        UnitOfMeasure eachUom=eachUomOptional.get();
        UnitOfMeasure tableSpoonUom=tableSpoonUomOptional.get();
        UnitOfMeasure teaSpoonUom=teaSpoonUomOptional.get();
        UnitOfMeasure dashUom=dashUomOptional.get();
        UnitOfMeasure pintUom=pintUomOptional.get();
        UnitOfMeasure cupsUom=pintUomOptional.get();

        Optional<Category> americanCategoryOptional=categoryRepository.findByDescription("American");
        if(!americanCategoryOptional.isPresent()){
            throw new RuntimeException("Expected Category Not Found");
        }

        Optional<Category> mexicanCategoryOptional=categoryRepository.findByDescription("Mexican");
        if(!mexicanCategoryOptional.isPresent()){
            throw new RuntimeException("Expected Category Not Found");
        }
        Category americanCategory=americanCategoryOptional.get();
        Category mexicanCategory=mexicanCategoryOptional.get();
        Recipe guacRecipe=new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(2);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("Add salt, lime juice, and the rest:\n" +
                "Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "Serve:\n" +
                "Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");

        Notes guacNotes=new Notes();
        guacNotes.setRecipeNotes("Guacamole is best eaten right after it's made. Like apples, avocados start to oxidize and turn brown once they've been cut. That said, the acid in the lime juice you add to guacamole can help slow down that process, and if you store the guacamole properly, you can easily make it a few hours ahead if you are preparing for a party.");
        guacNotes.setRecipe(guacRecipe);
        guacRecipe.setNotes(guacNotes);
        guacRecipe.getIngredients().add(new Ingredient("ripe avocados",new BigDecimal(2),eachUom,guacRecipe));
        guacRecipe.getIngredients().add(new Ingredient(" Kosher salt,",new BigDecimal(5),teaSpoonUom,guacRecipe));
        guacRecipe.getIngredients().add(new Ingredient("fresh lime juice or lemon juice",new BigDecimal(2),tableSpoonUom,guacRecipe));
        guacRecipe.getIngredients().add(new Ingredient("minced red onion or thinly sliced green onion",new BigDecimal(2),tableSpoonUom,guacRecipe));
        guacRecipe.getIngredients().add(new Ingredient(" serrano chiles, stems and seeds removed, minced",new BigDecimal(2),eachUom,guacRecipe));

        guacRecipe.getIngredients().add(new Ingredient("cilantro",new BigDecimal(2),tableSpoonUom,guacRecipe));

        guacRecipe.getIngredients().add(new Ingredient(" freshly grated black pepper",new BigDecimal(2),dashUom,guacRecipe));

        guacRecipe.getIngredients().add(new Ingredient("rripe tomato, seeds and pulp removed, chopped",new BigDecimal(5),eachUom,guacRecipe));
        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);
        guacRecipe.setUrl("http://www.simplyrecipes.com/recipes/perfect_guacamole");
        guacRecipe.setServings(4);
        guacRecipe.setSource("Simply Recipes");
        recipes.add(guacRecipe);
        Recipe tacosRecipe=new Recipe();
        tacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        tacosRecipe.setCookTime(9);
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);
        tacosRecipe.setDirections("Cod, tilapia, or halibut are my go-to choices" +
                " for fish tacos, but you could really use any white " +
                "fish fillets. It doesn't really matter if the fish is a firm variety or more " +
                "flakey; you break up the cooked fish into the tacos anyway, so perfect presentation isn't really important.\n" +
                "\n" +
                "Make your life easier and pick up filets that " +
                "have already been skinned. If you buy from a fish counter," +
                " you can always ask to have the skins removed for you at no extra " +
                "charge.");
        Notes tacoNotes=new Notes();
        tacoNotes.setRecipeNotes("I lightly coat my fish with chili powder before searing. It adds a touch of spice to the fish and makes a nice contrast to the cooling cabbage slaw.\n" +
                "\n" +
                "If you're not a fan of chili powder, use plain paprika or any favorite spice mix. Even just salt and pepper is just fine!");
        tacoNotes.setRecipe(tacosRecipe);
        tacosRecipe.setNotes(tacoNotes);
        tacosRecipe.getIngredients().add(new Ingredient("Ancho Chill Powder",new BigDecimal(2),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Dried Oregano",new BigDecimal(1),teaSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Dried Cumin",new BigDecimal(1),teaSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Sugar",new BigDecimal(1),teaSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Salt",new BigDecimal(5),teaSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Clove of Garlic, Choppedr",new BigDecimal(1),eachUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("finely grated orange zestr",new BigDecimal(1),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("fresh-squeezed orange juice",new BigDecimal(3),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Olive Oil",new BigDecimal(2),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Ancho Chill Powder",new BigDecimal(2),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("boneless chicken thighs",new BigDecimal(4),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("small corn torillasr",new BigDecimal(8),eachUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("packed baby arugula",new BigDecimal(3),cupsUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("medium ripe avocados,slic",new BigDecimal(2),eachUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("Ancho Chill Powder",new BigDecimal(2),tableSpoonUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("radishes, thinly sliced",new BigDecimal(4),eachUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("cherry tomatoes,halved",new BigDecimal(5),pintUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("red onion, thinly sliced",new BigDecimal(25),eachUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("cup sour cream thinned with 1/4 cup milk",new BigDecimal(4),cupsUom,tacosRecipe));
        tacosRecipe.getIngredients().add(new Ingredient("lime,cut into wedges",new BigDecimal(4),eachUom,tacosRecipe));
        tacosRecipe.getCategories().add(americanCategory);
        tacosRecipe.getCategories().add(mexicanCategory);
        tacosRecipe.setUrl("http://8080.taccosRecipe.com");
        tacosRecipe.setSource("Simple Recipe");
        tacosRecipe.setServings(7);
        recipes.add(tacosRecipe);
        return recipes;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("Loading Bootstrap Data");
        recipeRepository.saveAll(getRecipes());
    }
}
