package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private RecipeReactiveRepository recipeReactiveRepository;

	public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
		this.recipeReactiveRepository = recipeReactiveRepository;
	}

	@Override
	public Mono<Void> saveImageFile(String id, MultipartFile file) {

		log.debug("received a file");

		Mono<Recipe> recipeMono = recipeReactiveRepository.findById(id)
				.map(recipe -> {
					Byte[] byteObjects = new Byte[0];
					try {
						byteObjects = new Byte[file.getBytes().length];
						int i = 0;

						for(byte b : file.getBytes()){
							byteObjects[i++] = b;
						}
						recipe.setImage(byteObjects);
						return recipe;

					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException();
					}
				});

		recipeReactiveRepository.save(recipeMono.block()).block();

		return Mono.empty();
	}
}
