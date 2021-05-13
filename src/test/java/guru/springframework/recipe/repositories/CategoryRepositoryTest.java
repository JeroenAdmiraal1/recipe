package guru.springframework.recipe.repositories;

import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@DataJpaTest
class CategoryRepositoryTest {

	@Autowired
	CategoryRepository categoryRepository;

	@Test
	void findByDescription() {

		Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
		assertEquals("American", categoryOptional.get().getDescription());
	}
}