package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	IngredientService ingredientService;

	@InjectMocks
	IngredientController ingredientController;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		ingredientController = new IngredientController(recipeService, ingredientService);
		mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
	}

	@Test
	void getIngredients() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

		mockMvc.perform(get("/recipe/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/list"))
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyLong());
	}

	@Test
	void testShowIngredient() throws Exception {
		IngredientCommand ingredientCommand = new IngredientCommand();
		when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

		mockMvc.perform(get("/recipe/1/ingredient/2/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/show"))
				.andExpect(model().attributeExists("ingredient"));

		verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());

	}
}