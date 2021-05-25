package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

	Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

	Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);

	Mono<Void> deleteByRecipeAndIngredientId(String recipeId, String ingredientId);
}
