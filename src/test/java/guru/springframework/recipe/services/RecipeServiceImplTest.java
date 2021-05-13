package guru.springframework.recipe.services;

import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.converters.RecipeToRecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

	@Mock
	private RecipeToRecipeCommand recipeToRecipeCommand;

	@Mock
	private RecipeCommandToRecipe recipeCommandToRecipe;

	@Mock
	private RecipeRepository recipeRepository;

	@InjectMocks
	private RecipeServiceImpl recipeService;

	@Test
	void getRecipes() {
		Recipe recipe = new Recipe();
		HashSet recipesData = new HashSet();
		recipesData.add(recipe);

		when(recipeRepository.findAll()).thenReturn(recipesData);

		Set<Recipe> recipes = recipeService.getRecipes();

		assertEquals(recipes.size(), 1);
		verify(recipeRepository, times(1)).findAll();
	}

	@Test
	public void getRecipeByIdNotFound() {

		Optional<Recipe> recipeOptional = Optional.empty();
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

		Exception expectedException = assertThrows(NotFoundException.class, () -> {
			Recipe recipeReturned = recipeService.findById("1");
		});

		String expectedMessage = "Recipe Not Found";
		String actualMessage = expectedException.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}



	@Test
	void getRecipeByIdTest() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId("1");
		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

		Recipe returnedRecipe = recipeService.findById("1");

		assertNotNull(returnedRecipe);
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, never()).findAll();

	}

	@Test
	void testDeleteById() throws Exception {
		String idToBeDeleted = "2";
		recipeService.deleteById(idToBeDeleted);

		verify(recipeRepository, times(1)).deleteById(anyString());
	}

}