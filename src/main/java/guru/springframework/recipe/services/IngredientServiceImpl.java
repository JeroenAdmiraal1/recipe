package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.converters.IngredientCommandToIngredient;
import guru.springframework.recipe.converters.IngredientToIngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final RecipeReactiveRepository recipeReactiveRepository;
	private final RecipeRepository recipeRepository;
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeReactiveRepository recipeReactiveRepository
			, RecipeRepository recipeRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.recipeRepository = recipeRepository;
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
	}

	@Override
	public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		return recipeReactiveRepository
				.findById(recipeId)
				.flatMapIterable(Recipe::getIngredients)
				.filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
				.single()
				.map(ingredient -> {
					IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
					command.setRecipeId(recipeId);
					return command;
				});
	}

	@Override
	@Transactional
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

		if(!recipeOptional.isPresent()){
			return Mono.just(new IngredientCommand());
		} else {
			Recipe recipe = recipeOptional.get();
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
					                                          .filter(ingredient -> ingredient.getId().equals(command.getId()))
					                                          .findFirst();

			if(ingredientOptional.isPresent()){
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				ingredientFound.setUnitOfMeasure(unitOfMeasureReactiveRepository
						                       .findById(command.getUnitOfMeasure().getId()).block());
//						                       .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
			} else {
				//add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(command);
				recipe.addIngredient(ingredient);
			}

			Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
					                                               .stream()
					                                               .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
					                                               .findFirst();
			//check by description
			if(!savedIngredientOptional.isPresent()){
				savedIngredientOptional = savedRecipe.getIngredients()
						                          .stream()
						                          .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
						                          .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
						                          .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
						                          .findFirst();
			}
			return Mono.just(ingredientToIngredientCommand.convert(savedIngredientOptional.get()));
		}
	}

	@Override
	public Mono<Void> deleteByRecipeAndIngredientId(String recipeId, String ingredientId) {
		log.debug("Deleting ingredient: " + recipeId + ":" + ingredientId);

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if(recipeOptional.isPresent()){
			Recipe recipe = recipeOptional.get();
			log.debug("found recipe");

			Optional<Ingredient> ingredientOptional = recipe.getIngredients()
					                                          .stream()
					                                          .filter(ingredient -> ingredient.getId().equals(ingredientId))
					                                          .findFirst();

			if(ingredientOptional.isPresent()){
				log.debug("found Ingredient");
				Ingredient ingredientToDelete = ingredientOptional.get();
				recipe.getIngredients().remove(ingredientOptional.get());
				recipeRepository.save(recipe);
			}
		} else {
			log.debug("Recipe Id Not found. Id:" + recipeId);
		}
		return Mono.empty();
	}

}
