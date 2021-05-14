package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.converters.IngredientCommandToIngredient;
import guru.springframework.recipe.converters.IngredientToIngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final RecipeRepository recipeRepository;
	private final UnitOfMeasureRepository unitOfMeasureRepository;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeRepository = recipeRepository;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if(!recipeOptional.isPresent()){
			log.error("recipe not found, id:" + recipeId);
		}

		Recipe recipe = recipeOptional.get();

		Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
				                                                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
				                                                        .map(ingredientToIngredientCommand::convert)
				                                                        .findFirst();
		if(!ingredientCommandOptional.isPresent()){
			log.error("ingredient not found, id:" + ingredientId);
		}

		return ingredientCommandOptional.get();
	}

	@Override
	@Transactional
	public IngredientCommand saveIngredientCommand(IngredientCommand command) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

		if(!recipeOptional.isPresent()){
			return new IngredientCommand();
		} else {
			Recipe recipe = recipeOptional.get();
			Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
					                                          .filter(ingredient -> ingredient.getId().equals(command.getId()))
					                                          .findFirst();

			if(ingredientOptional.isPresent()){
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
						                       .findById(command.getUnitOfMeasure().getId())
						                       .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
			} else {
				//add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(command);
				recipe.addIngredient(ingredient);
			}

			Recipe savedRecipe = recipeRepository.save(recipe);

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
			return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
		}
	}

	@Override
	public void deleteByRecipeAndIngredientId(String recipeId, String ingredientId) {
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
	}

}
