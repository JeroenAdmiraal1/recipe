package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;


	public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
	}

	@GetMapping("/recipe/{id}/ingredients")
	public String getIngredients(@PathVariable String id, Model model){
		log.debug("getting ingredient list for recipe:" + id);
		RecipeCommand recipe = recipeService.findCommandById(Long.valueOf(id));

		model.addAttribute("recipe", recipe);

		return "recipe/ingredient/list";
	}

	@GetMapping("/recipe/{id}/ingredient/{ingredientId}/show")
	public String showRecipeIngredient(@PathVariable String id, @PathVariable String ingredientId, Model model){
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(id), Long.valueOf(ingredientId)));
		return "recipe/ingredient/show";
	}
}
