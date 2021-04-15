package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

	private NotesCommandToNotes notesConverter;
	private IngredientCommandToIngredient ingredientConverter;
	private CategoryCommandToCategory categoryConverter;

	public RecipeCommandToRecipe(NotesCommandToNotes notesConverter, IngredientCommandToIngredient ingredientConverter, CategoryCommandToCategory categoryConverter) {
		this.notesConverter = notesConverter;
		this.ingredientConverter = ingredientConverter;
		this.categoryConverter = categoryConverter;
	}

	@Nullable
	@Synchronized
	@Override
	public Recipe convert(RecipeCommand command) {
		if (command == null){
			return null;
		}

		final Recipe recipe = new Recipe();
		recipe.setSource(command.getSource());
		recipe.setUrl(command.getUrl());
		recipe.setServings(command.getServings());
		recipe.setId(command.getId());
		recipe.setDescription(command.getDescription());
		recipe.setDifficulty(command.getDifficulty());
		recipe.setCookTime(command.getCookTime());
		recipe.setPrepTime(command.getPrepTime());
		recipe.setDirections(command.getDirections());
		recipe.setUrl(command.getUrl());

		if(command.getImage() != null){
			recipe.setImage(command.getImage());
		}

		if(command.getNotes() != null){
			recipe.setNotes(notesConverter.convert(command.getNotes()));
		}

		if (command.getCategories() != null && command.getCategories().size() > 0){
			command.getCategories()
					.forEach(category -> recipe.getCategories().add(categoryConverter.convert(category)));
		}

		if (command.getIngredients() != null && command.getIngredients().size() > 0){
			command.getIngredients()
					.forEach(ingredient -> recipe.getIngredients().add(ingredientConverter.convert(ingredient)));
		}
		return recipe;
	}
}
