package guru.springframework.recipe.controllers;

import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;


import java.util.Set;

@Slf4j
@Controller
public class IndexController {


	private final RecipeService recipeService;

	public IndexController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping({"", "/", "/index"})
	public String getIndexPage(Model model){
		log.debug("I'm at the indexController");

		Flux<Recipe> recipes = recipeService.getRecipes();

		model.addAttribute("recipes", recipes.collectList().block());

		System.out.println("some message..12345");
		return "index";
	}
}
