package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;


public interface RecipeService {

	Flux<Recipe> getRecipes();

	Mono<Recipe> findById(String l);

	Mono<RecipeCommand> findCommandById(String l);

	Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

	Mono<Void> deleteById(String l);
}
