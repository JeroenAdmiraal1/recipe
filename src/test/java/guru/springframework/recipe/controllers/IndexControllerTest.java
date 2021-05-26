package guru.springframework.recipe.controllers;

import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.services.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

	@Mock
	private RecipeServiceImpl recipeService;
	@Mock
	private Model model;

	private IndexController indexController;

	@BeforeEach
	void setUp(){
		indexController = new IndexController(recipeService);
	}

	@Test
	void testMockMVC() throws Exception{
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

		when(recipeService.getRecipes()).thenReturn(Flux.empty());

		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("index"));
	}

	@Test
	void getIndexPage() {
		//given
		Set<Recipe> recipesData = new HashSet<>();
		recipesData.add(new Recipe());

		Recipe recipe = new Recipe();
		recipe.setId("2");
		recipesData.add(recipe);

		ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

		when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipesData));

		//when
		String expectedString = "index";
		String actualString = indexController.getIndexPage(model);

		//then
		assertEquals(expectedString, actualString);
		verify(model,times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
		verify(recipeService,times(1)).getRecipes();
		List<Recipe> recipeList = argumentCaptor.getValue();
		assertEquals(2, recipeList.size());
	}
}