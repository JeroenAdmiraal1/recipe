package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.converters.RecipeCommandToRecipe;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

	@Mock
	RecipeReactiveRepository recipeReactiveRepository;

	@InjectMocks
	ImageServiceImpl imageService;

	@BeforeEach
	public void setUp() throws Exception {
		imageService = new ImageServiceImpl(recipeReactiveRepository);
	}

	@Test
	public void saveImageFile() throws IOException {
		String id = "1";
		MultipartFile file = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Somestring".getBytes());

		Recipe recipe = new Recipe();
		recipe.setId(id);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
		when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));


		ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

		imageService.saveImageFile(id, file);

		verify(recipeReactiveRepository,times(1)).save(argumentCaptor.capture());
		Recipe savedRecipe = argumentCaptor.getValue();
		assertEquals(file.getBytes().length, savedRecipe.getImage().length);
	}

}