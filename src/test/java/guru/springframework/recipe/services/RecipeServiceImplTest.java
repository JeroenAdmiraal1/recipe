package guru.springframework.recipe.services;

import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.converters.RecipeToRecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
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
	RecipeReactiveRepository recipeReactiveRepository;

	@InjectMocks
	private RecipeServiceImpl recipeService;

	@Test
	void getRecipes() {
		Recipe recipe = new Recipe();

		when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));

		List<Recipe> recipes = recipeService.getRecipes().collectList().block();

		assertEquals(recipes.size(), 1);
		verify(recipeReactiveRepository, times(1)).findAll();
	}

	@Test
	void getRecipeByIdTest() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId("1");

		when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

		Recipe returnedRecipe = recipeService.findById("1").block();

		assertNotNull(returnedRecipe);
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, never()).findAll();

	}

	@Test
	void testDeleteById() throws Exception {
		String idToBeDeleted = "2";

		when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

		recipeService.deleteById(idToBeDeleted);

		verify(recipeReactiveRepository, times(1)).deleteById(anyString());
	}

}