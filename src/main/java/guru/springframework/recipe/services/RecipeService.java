package guru.springframework.recipe.services;

import guru.springframework.recipe.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface RecipeService {

	Set<Recipe> getRecipes();

	Recipe findById(Long l);
}
