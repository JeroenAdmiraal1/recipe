package guru.springframework.recipe.controllers;

import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.services.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

	private IndexController indexController;

	@Mock
	private RecipeServiceImpl recipeService;
	@Mock
	private Model model;

	@BeforeEach
	void setUp(){
		indexController = new IndexController(recipeService);
	}

	@Test
	void getIndexPage() {
		Recipe recipe = new Recipe();
		HashSet recipesData = new HashSet();
		recipesData.add(recipe);

		when(recipeService.getRecipes()).thenReturn(recipesData);

		String expectedString = "index";
		String actualString = indexController.getIndexPage(model);

		assertEquals(expectedString, actualString);
		verify(model,times(1)).addAttribute(eq("recipes"), anySet());
		verify(recipeService,times(1)).getRecipes();

	}
}