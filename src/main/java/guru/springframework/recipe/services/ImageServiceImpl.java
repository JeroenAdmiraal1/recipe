package guru.springframework.recipe.services;

import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	@Transactional
	public void saveImageFile(Long id, MultipartFile file) {

		log.debug("received a file");

		try {
			Recipe recipe = recipeRepository.findById(id).get();
			Byte[] byteArray = new Byte[file.getBytes().length];

			int i = 0;

			for(byte b : file.getBytes()){
				byteArray[i++] = b;
			}
			recipe.setImage(byteArray);
			recipeRepository.save(recipe);

		} catch (IOException e) {
			log.error("error occured, e");
			e.printStackTrace();
		}
	}
}
