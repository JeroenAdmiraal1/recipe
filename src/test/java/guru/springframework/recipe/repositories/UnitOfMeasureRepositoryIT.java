package guru.springframework.recipe.repositories;

import guru.springframework.recipe.bootstrap.RecipeBootstrap;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureRepositoryIT {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RecipeRepository recipeRepository;


	@BeforeEach
	public void setUp() throws Exception {
		recipeRepository.deleteAll();
		unitOfMeasureRepository.deleteAll();
		categoryRepository.deleteAll();
		RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);
		recipeBootstrap.onApplicationEvent(null);
	}

	@Test
	void findByDescriptionTeaspoon() throws Exception{

		Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
		assertEquals("Teaspoon", unitOfMeasureOptional.get().getDescription());
	}

	@Test
	void findByDescriptionCup() throws Exception{

		Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Cup");
		assertEquals("Cup", unitOfMeasureOptional.get().getDescription());
	}
}