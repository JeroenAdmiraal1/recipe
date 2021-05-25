package guru.springframework.recipe.repositories.reactive;

import guru.springframework.recipe.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class RecipeReactiveRepositoryTest {

	@Autowired
	RecipeReactiveRepository recipeReactiveRepository;

	@BeforeEach
	void setUp() {
		recipeReactiveRepository.deleteAll().block();
	}

	@Test
	public void testSave(){
		Recipe recipe = new Recipe();
		recipe.setDescription("Yummy");

		recipeReactiveRepository.save(recipe).block();

		Long count = recipeReactiveRepository.count().block();

		assertEquals(Long.valueOf(1), count);
	}
}