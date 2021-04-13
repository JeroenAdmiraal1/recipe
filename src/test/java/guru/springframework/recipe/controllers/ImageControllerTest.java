package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.services.ImageService;
import guru.springframework.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

	@Mock
	ImageService imageService;

	@Mock
	RecipeService recipeService;

	ImageController controller;

	MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {

		controller = new ImageController(imageService, recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void getImageForm() throws Exception {
		//given
		RecipeCommand command = new RecipeCommand();
		command.setId(1L);

		when(recipeService.findCommandById(anyLong())).thenReturn(command);

		//when
		mockMvc.perform(get("/recipe/1/image"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyLong());

	}

	@Test
	public void handleImagePost() throws Exception {
		MockMultipartFile multipartFile =
				new MockMultipartFile("imagefile", "testing.txt", "text/plain",
						"Spring Framework Guru".getBytes());

		mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/recipe/1/show"));

		verify(imageService, times(1)).saveImageFile(anyLong(), any());
	}

	@Test
	public void renderImageFromDB() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(1L);

		String s = "fake image text";
		Byte[] bytesBoxed = new Byte[s.getBytes().length];

		int i = 0;

		for(byte b : s.getBytes()){
			bytesBoxed[i++] = b;
		}

		recipeCommand.setImage(bytesBoxed);
		when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

		MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
				                                   .andExpect(status().isOk())
				                                   .andReturn().getResponse();

		byte[] responseBytes = response.getContentAsByteArray();

		assertEquals(s.getBytes().length, responseBytes.length);

	}

}