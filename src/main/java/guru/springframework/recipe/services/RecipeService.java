package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface RecipeService {

	Set<Recipe> getRecipes();

	Recipe findById(String l);

	RecipeCommand findCommandById(String l);

	RecipeCommand saveRecipeCommand(RecipeCommand command);

	void deleteById(String l);
}
