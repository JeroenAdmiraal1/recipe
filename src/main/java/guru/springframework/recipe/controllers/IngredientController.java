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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	private WebDataBinder webDataBinder;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@InitBinder("ingredient")
	public void initBinder(WebDataBinder webDataBinder){
		this.webDataBinder = webDataBinder;
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

		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("/recipe/{id}/ingredient/{ingredientId}/update")
	public String updateRecipeIngredient(@PathVariable String id, @PathVariable String ingredientId, Model model){
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(id, ingredientId));
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("recipe/{recipeId}/ingredient")
	public Mono<String> saveOrUpdate(@ModelAttribute IngredientCommand command, @PathVariable String recipeId, Model model){

		webDataBinder.validate();
		BindingResult bindingResult = webDataBinder.getBindingResult();

		if(bindingResult.hasErrors()) {

			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});

			return Mono.just("recipe/ingredient/ingredientform");
		}

		//IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();
		Mono<String> redirect = ingredientService
				                        .saveIngredientCommand(command)
				                        .map(savedCommand -> "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show");

		return redirect;
	}

	@GetMapping("/recipe/{id}/ingredient/{ingredientId}/delete")
	public String deleteIngredient(@PathVariable String id, @PathVariable String ingredientId){
		log.debug("deleting ingredient by id: " + ingredientId );
		ingredientService.deleteByRecipeAndIngredientId(id, ingredientId);
		return "redirect:/recipe/" + id + "/ingredients";
	}

	@ModelAttribute("unitOfMeasureList")
	public Flux<UnitOfMeasureCommand> populateUnitOfMeasureList(){
		return unitOfMeasureService.listAllUnitOfMeasure();
	}

}
