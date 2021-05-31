package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import guru.springframework.recipe.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	IngredientService ingredientService;

	@Mock
	UnitOfMeasureService unitOfMeasureService;

	@InjectMocks
	IngredientController ingredientController;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
		mockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
				          .setControllerAdvice(new ControllerExceptionHandler()).build();
	}

	@Test
	void getIngredients() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

		mockMvc.perform(get("/recipe/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/list"))
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyString());
	}

	@Test
	void testShowIngredient() throws Exception {
		IngredientCommand ingredientCommand = new IngredientCommand();
		when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));

		mockMvc.perform(get("/recipe/1/ingredient/2/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/show"))
				.andExpect(model().attributeExists("ingredient"));

		verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(), anyString());

	}

	@Test
	void testNewIngredientForm() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("1");

		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));
		when(unitOfMeasureService.listAllUnitOfMeasure()).thenReturn(Flux.just(new UnitOfMeasureCommand()));

		mockMvc.perform(get("/recipe/1/ingredient/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/ingredientform"))
				.andExpect(model().attributeExists("ingredient"))
				.andExpect(model().attributeExists("unitOfMeasureList"));

		verify(recipeService, times(1)).findCommandById(anyString());
	}

	@Test
	void testUpdateIngredientForm() throws Exception {
		IngredientCommand ingredientCommand = new IngredientCommand();
		when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));
		when(unitOfMeasureService.listAllUnitOfMeasure()).thenReturn(Flux.just(new UnitOfMeasureCommand()));

		mockMvc.perform(get("/recipe/1/ingredient/2/update"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/ingredientform"))
				.andExpect(model().attributeExists("ingredient"))
				.andExpect(model().attributeExists("unitOfMeasureList"));
	}

	@Test
	void testSaveOrUpdate() throws Exception {
		IngredientCommand command = new IngredientCommand();
		command.setId("3");
		command.setRecipeId("2");

		when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(command));

		mockMvc.perform(post("/recipe/2/ingredient")
				                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
				                .param("id", "")
				                .param("description", "somestring"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
	}

	@Test
	public void testDeleteIngredient() throws Exception {

		when(ingredientService.deleteByRecipeAndIngredientId(anyString(), anyString())).thenReturn(Mono.empty());

		mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredients"));

		verify(ingredientService, times(1)).deleteByRecipeAndIngredientId(anyString(), anyString());
	}
;
}