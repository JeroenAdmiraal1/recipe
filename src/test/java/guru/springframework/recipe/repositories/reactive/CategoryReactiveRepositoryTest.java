package guru.springframework.recipe.repositories.reactive;

import guru.springframework.recipe.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CategoryReactiveRepositoryTest {

	@Autowired
	CategoryReactiveRepository categoryReactiveRepository;

	@BeforeEach
	void setUp() {
		categoryReactiveRepository.deleteAll().block();
	}

	@Test
	public void testSave(){
		Category category = new Category();
		category.setDescription("category");
		categoryReactiveRepository.save(category).block();

		Long count = categoryReactiveRepository.count().block();

		assertEquals(Long.valueOf(1), count);
	}

	@Test
	public void testFindByDescription(){
		Category category = new Category();
		category.setDescription("foo");
		categoryReactiveRepository.save(category).block();

		Category foundCat = categoryReactiveRepository.findByDescription("foo").block();

		assertNotNull(foundCat.getId());
	}
}