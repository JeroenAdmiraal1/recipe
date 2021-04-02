package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

	private NotesToNodesCommand notesConverter;
	private IngredientToIngredientCommand ingredientConverter;
	private CategoryToCategoryCommand categoryConverter;

	public RecipeToRecipeCommand(NotesToNodesCommand notesConverter, IngredientToIngredientCommand ingredientConverter, CategoryToCategoryCommand categoryConverter) {
		this.notesConverter = notesConverter;
		this.ingredientConverter = ingredientConverter;
		this.categoryConverter = categoryConverter;
	}

	@Nullable
	@Synchronized
	@Override
	public RecipeCommand convert(Recipe recipe) {
		if (recipe == null) {
			return null;
		}

		final RecipeCommand command = new RecipeCommand();
		command.setId(recipe.getId());
		command.setCookTime(recipe.getCookTime());
		command.setPrepTime(recipe.getPrepTime());
		command.setDescription(recipe.getDescription());
		command.setDifficulty(recipe.getDifficulty());
		command.setDirections(recipe.getDirections());
		command.setServings(recipe.getServings());
		command.setSource(recipe.getSource());
		command.setUrl(recipe.getUrl());

		if(recipe.getNotes() != null){
			command.setNotes(notesConverter.convert(recipe.getNotes()));
		}

		if (recipe.getCategories() != null && recipe.getCategories().size() > 0){
			recipe.getCategories()
					.forEach((Category category) -> command.getCategories().add(categoryConverter.convert(category)));
		}

		if (recipe.getIngredients() != null && recipe.getIngredients().size() > 0){
			recipe.getIngredients()
					.forEach(ingredient -> command.getIngredients().add(ingredientConverter.convert(ingredient)));
		}

		return command;
	}
}
