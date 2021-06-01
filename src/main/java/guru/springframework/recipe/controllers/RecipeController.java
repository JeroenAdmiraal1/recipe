package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.exceptions.TemplateInputException;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
@Slf4j
public class RecipeController {

	private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
	private final RecipeService recipeService;
	private WebDataBinder webDataBinder;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/show")
	public String showById(@PathVariable String id, Model model){
		model.addAttribute("recipe", recipeService.findById(id));
		return "recipe/show";
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder){
		this.webDataBinder = webDataBinder;
	}

	@GetMapping("/recipe/new")
	public String newRecipe(Model model){
		model.addAttribute("recipe", new RecipeCommand());

		return RECIPE_RECIPEFORM_URL;
	}

	@GetMapping("/recipe/{id}/update")
	public String updateRecipe(@PathVariable String id, Model model){
		model.addAttribute("recipe", recipeService.findCommandById(id));
		return RECIPE_RECIPEFORM_URL;
	}

	@PostMapping("recipe")
	public Mono<String> saveOrUpdate(@ModelAttribute("recipe") RecipeCommand command){
		webDataBinder.validate();
		BindingResult bindingResult = webDataBinder.getBindingResult();

		if(bindingResult.hasErrors()){

			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});

		//	return RECIPE_RECIPEFORM_URL;
			return Mono.just(RECIPE_RECIPEFORM_URL);
		}

	//	RecipeCommand savedCommand = recipeService.saveRecipeCommand(command).block();
		Mono<String> redirect = recipeService
				                        .saveRecipeCommand(command)
				                        .map(savedRecipe -> "redirect:/recipe/" + savedRecipe.getId() + "/show" );

		return redirect;
		// return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}

	@GetMapping("/recipe/{id}/delete")
	public String deleteById(@PathVariable String id){
		log.debug("deleting id: " + id );
		recipeService.deleteById(id);
		return "redirect:/";
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NotFoundException.class, TemplateInputException.class})
	public String handleNotFound(Exception e, Model model){

		model.addAttribute("exception", e);

		return "recipe/404error";

	}

}
