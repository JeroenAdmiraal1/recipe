package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import guru.springframework.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@GetMapping("/recipe/{id}/ingredients")
	public String getIngredients(@PathVariable String id, Model model){
		log.debug("getting ingredient list for recipe:" + id);

		model.addAttribute("recipe", recipeService.findCommandById(id));

		return "recipe/ingredient/list";
	}

	@GetMapping("/recipe/{id}/ingredient/{ingredientId}/show")
	public String showRecipeIngredient(@PathVariable String id, @PathVariable String ingredientId, Model model){
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(id, ingredientId));
		return "recipe/ingredient/show";
	}

	@GetMapping("/recipe/{id}/ingredient/new")
	public String newRecipeIngredient(@PathVariable String id, Model model){
		RecipeCommand recipeCommand = recipeService.findCommandById(id).block();

		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(id);
		model.addAttribute("ingredient",ingredientCommand);
		ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

		model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitOfMeasure());
		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("/recipe/{id}/ingredient/{ingredientId}/update")
	public String updateRecipeIngredient(@PathVariable String id, @PathVariable String ingredientId, Model model){
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(id, ingredientId));
		model.addAttribute("unitOfMeasureList", unitOfMeasureService.listAllUnitOfMeasure());
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command){
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

		log.debug("saved receipe id:" + savedCommand.getRecipeId());
		log.debug("saved ingredient id:" + savedCommand.getId());

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}

	@GetMapping("/recipe/{id}/ingredient/{ingredientId}/delete")
	public String deleteIngredient(@PathVariable String id, @PathVariable String ingredientId){
		log.debug("deleting ingredient by id: " + ingredientId );
		ingredientService.deleteByRecipeAndIngredientId(id, ingredientId);
		return "redirect:/recipe/" + id + "/ingredients";
	}
}
