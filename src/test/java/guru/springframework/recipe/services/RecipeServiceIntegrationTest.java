package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.converters.RecipeToRecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
class RecipeServiceIntegrationTest {

	public static final String NEW_DESCRIPTION = "New Description";

	@Autowired
	RecipeService recipeService;

	@Autowired
	RecipeToRecipeCommand recipeToRecipeCommand;

	@Autowired
	RecipeCommandToRecipe recipeCommandToRecipe;

	@Autowired
	RecipeRepository recipeRepository;

	@Test
	@Transactional
	void saveRecipeCommand() throws Exception{
		//given
		Iterable<Recipe> recipes = recipeRepository.findAll();
		Recipe testRecipe = recipes.iterator().next();
		RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

		//when
		testRecipeCommand.setDescription(NEW_DESCRIPTION);
		RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

		//then
		assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
		assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
		assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
		assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
	}
}