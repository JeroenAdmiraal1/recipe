package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.converters.IngredientCommandToIngredient;
import guru.springframework.recipe.converters.IngredientToIngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final RecipeReactiveRepository recipeReactiveRepository;
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
	                             IngredientCommandToIngredient ingredientCommandToIngredient,
	                             RecipeReactiveRepository recipeReactiveRepository,
	                             UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeReactiveRepository = recipeReactiveRepository;
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
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {

		Objects.requireNonNull(command);
		AtomicReference<String> ingredientId = new AtomicReference<>();
		AtomicReference<String> recipeId = new AtomicReference<>();
		return recipeReactiveRepository
				       .findById(command.getRecipeId())
				       .map(recipe -> {
					       recipeId.set(recipe.getId());
					       recipe.getIngredients().stream()
							       .filter(ingredient -> ingredient.getId().equalsIgnoreCase(command.getId()))
							       .findFirst()
							       .map(ingredient -> {
								       ingredientId.set(command.getId());
								       ingredient.setDescription(command.getDescription());
								       ingredient.setAmount(command.getAmount());
								       return recipe;
							       }).orElseGet(() -> {
								       Ingredient newIngredient = ingredientCommandToIngredient.convert(command);
								       ingredientId.set(Objects.requireNonNull(newIngredient).getId());
								       unitOfMeasureReactiveRepository
										       .findById(command.getUnitOfMeasure().getId())
										       .flatMap(unitOfMeasure -> {
											       newIngredient.setUnitOfMeasure(unitOfMeasure);
											       return Mono.just(unitOfMeasure);
										       }).subscribe();
								       recipe.addIngredient(newIngredient);
								       return recipe;
							       });
					       return recipe;
				       })
				       .flatMap(recipe -> recipeReactiveRepository.save(recipe).then(Mono.just(recipe)))
				       .flatMapIterable(Recipe::getIngredients)
				       .filter(savedIngredient -> savedIngredient.getId().equalsIgnoreCase(ingredientId.get()))
				       .flatMap(savedIngredient -> {
					       IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(savedIngredient);
					       ingredientCommand.setRecipeId(recipeId.get());
					       return Mono.justOrEmpty(ingredientCommand);
				       }).single();
//		Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();
//
//		if(recipe == null){
//			log.error("Recipe not found for id: " + command.getRecipeId());
//			return Mono.just(new IngredientCommand());
//		} else {
//
//			Optional<Ingredient> ingredientOptional = recipe.getIngredients()
//					                                          .stream()
//					                                          .filter(ingredient -> ingredient.getId().equals(command.getId()))
//					                                          .findFirst();
//
//			if(!ingredientOptional.isPresent()){
//				saveNewIngredientToRecipe(command, recipe);
//			} else {
//				updateExistingIngredientInRecipe(command, ingredientOptional);
//			}
//
//			Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();
//
//			Optional<Ingredient> savedIngredientOptional = getNewOrUpdatedIngredientFromTheRecipe(command, savedRecipe);
//
//			IngredientCommand ingredientCommandNew = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
//			ingredientCommandNew.setRecipeId(recipe.getId());
//
//			return Mono.just(ingredientCommandNew);


	}

	private void updateExistingIngredientInRecipe(IngredientCommand command, Optional<Ingredient> ingredientOptional) {
		Ingredient ingredientFound = ingredientOptional.get();
		ingredientFound.setDescription(command.getDescription());
		ingredientFound.setAmount(command.getAmount());
		ingredientFound.setUnitOfMeasure(unitOfMeasureReactiveRepository
				                                 .findById(command.getUnitOfMeasure().getId()).block());
	}

	private void saveNewIngredientToRecipe(IngredientCommand command, Recipe recipe) {
		Ingredient ingredient = ingredientCommandToIngredient.convert(command);
		recipe.addIngredient(ingredient);
	}

	private Optional<Ingredient> getNewOrUpdatedIngredientFromTheRecipe(IngredientCommand command, Recipe savedRecipe) {
		Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
				                                               .stream()
				                                               .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
				                                               .findFirst();

		if(!savedIngredientOptional.isPresent()){
			savedIngredientOptional = savedRecipe.getIngredients()
					                          .stream()
					                          .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
					                          .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
					                          .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
					                          .findFirst();
		}
		return savedIngredientOptional;
	}

	@Override
	public Mono<Void> deleteByRecipeAndIngredientId(String recipeId, String ingredientId) {
		log.debug("Deleting ingredient: " + recipeId + ":" + ingredientId);

		Recipe recipe = recipeReactiveRepository.findById(recipeId).block();
		if(recipe != null){
			log.debug("found recipe");

			Optional<Ingredient> ingredientOptional = recipe.getIngredients()
					                                          .stream()
					                                          .filter(ingredient -> ingredient.getId().equals(ingredientId))
					                                          .findFirst();

			if(ingredientOptional.isPresent()){
				log.debug("found Ingredient");
				Ingredient ingredientToDelete = ingredientOptional.get();
				recipe.getIngredients().remove(ingredientOptional.get());
				recipeReactiveRepository.save(recipe).block();
			}
		} else {
			log.debug("Recipe Id Not found. Id:" + recipeId);
		}
		return Mono.empty();
	}

}
