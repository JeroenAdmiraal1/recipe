package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.services.RecipeService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

	MockMvc mockMvc;

	@Mock
	RecipeService recipeService;

	@InjectMocks
	RecipeController controller;

	@BeforeEach
	public void setUp() throws Exception {
		controller = new RecipeController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				          .setControllerAdvice(new ControllerExceptionHandler()).build();
	}

	@Test
	public void testGetRecipe() throws Exception {

		Recipe recipe = new Recipe();
		recipe.setId(1L);

		when(recipeService.findById(anyLong())).thenReturn(recipe);

		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/show"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testGetRecipeNotFound() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isNotFound())
				.andExpect(view().name("recipe/404error"));
	}

	@Test
	public void testBadRequest() throws Exception {
		mockMvc.perform(get("/recipe/asdf/show"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("recipe/400error"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {

		mockMvc.perform(get("/recipe/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {

		RecipeCommand command = new RecipeCommand();
		RecipeCommand savedCommand = new RecipeCommand();
		savedCommand.setId(1L);

		when(recipeService.saveRecipeCommand(any())).thenReturn(savedCommand);

		mockMvc.perform(post("/recipe", command)
				                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
				                .param("id", "")
				                .param("description", "some string")
				                .param("directions", "some directions"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/1/show"));
	}

	@Test
	public void testPostNewRecipeFormValidationFail() throws Exception {

		mockMvc.perform(post("/recipe")
				                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
				                .param("id", "")

		)
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("recipe"))
				.andExpect(view().name("recipe/recipeform"));
	}

	@Test
	public void testGetUpdatedView() throws Exception {
		RecipeCommand savedCommand = new RecipeCommand();
		savedCommand.setId(1L);

		when(recipeService.findCommandById(anyLong())).thenReturn(savedCommand);

		mockMvc.perform(get("/recipe/1/update"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testDeleteById() throws Exception {
		mockMvc.perform(get("/recipe/1/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));

		verify(recipeService, times(1)).deleteById(anyLong());
	}
}