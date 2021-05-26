package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.converters.RecipeToRecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeReactiveRepository recipeReactiveRepository;
	private final RecipeCommandToRecipe recipeCommandToRecipe;
	private final RecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
	                         RecipeCommandToRecipe recipeCommandToRecipe,
	                         RecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Flux<Recipe> getRecipes() {
		return recipeReactiveRepository.findAll();
	}

	@Override
	public Mono<Recipe> findById(String id) {
		return recipeReactiveRepository.findById(id);
	}

	@Override
	public Mono<RecipeCommand> findCommandById(String id) {
		return recipeReactiveRepository.findById(id)
				       .map(recipe -> {
					       RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

					       recipeCommand.getIngredients().forEach(rc -> {
						       rc.setRecipeId(recipeCommand.getId());
					       });

					       return recipeCommand;
				       });
	}

	@Override
	public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
		return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
				       .map(recipeToRecipeCommand::convert);
	}

	@Override
	@Transactional
	public Mono<Void> deleteById(String l) {
		recipeReactiveRepository.deleteById(l).block();
		return Mono.empty();
	}
}
