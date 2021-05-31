package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.services.ImageService;
import guru.springframework.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Disabled
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
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				          .setControllerAdvice(new ControllerExceptionHandler()).build();
	}

	@Test
	public void getImageForm() throws Exception {
		//given
		RecipeCommand command = new RecipeCommand();
		command.setId("1");

		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

		//when
		mockMvc.perform(get("/recipe/1/image"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyString());

	}

	@Test
	public void handleImagePost() throws Exception {
		MockMultipartFile multipartFile =
				new MockMultipartFile("imagefile", "testing.txt", "text/plain",
						"Spring Framework Guru".getBytes());

		when(imageService.saveImageFile(anyString(), any())).thenReturn(Mono.empty());

		mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/recipe/1/show"));

		verify(imageService, times(1)).saveImageFile(anyString(), any());
	}

	@Disabled
	@Test
	public void renderImageFromDB() throws Exception {
//		RecipeCommand recipeCommand = new RecipeCommand();
//		recipeCommand.setId("1");
//
//		String s = "fake image text";
//		Byte[] bytesBoxed = new Byte[s.getBytes().length];
//
//		int i = 0;
//
//		for(byte b : s.getBytes()){
//			bytesBoxed[i++] = b;
//		}
//
//		recipeCommand.setImage(bytesBoxed);
//		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));
//
//		MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
//				                                   .andExpect(status().isOk())
//				                                   .andReturn().getResponse();
//
//		byte[] responseBytes = response.getContentAsByteArray();
//
//		assertEquals(s.getBytes().length, responseBytes.length);

	}

}