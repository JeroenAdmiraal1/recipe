package guru.springframework.recipe.controllers;

import guru.springframework.recipe.config.WebConfig;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouterFunctionTest {

	WebTestClient webTestClient;

	@Mock
	RecipeService recipeService;

	@BeforeEach
	public void setUp() throws Exception {
		WebConfig webConfig = new WebConfig();
		RouterFunction<?> routerFunction = webConfig.routes(recipeService);

		webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
	}

  @Test
	public void testGetRecipes() {

		when(recipeService.getRecipes()).thenReturn(Flux.just());

		webTestClient.get()
				.uri("/api/recipes")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testGetRecipesWithData() {

		when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

		webTestClient.get()
				.uri("/api/recipes")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Recipe.class);
	}
}
