package guru.springframework.recipe.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

	Category category;

	@BeforeEach
	public void setUp(){
		category = new Category();
	}

	@Test
	void getId() {
		Long idValue = 42L;
		category.setId(idValue);
		assertEquals(idValue, category.getId());
	}

	@Test
	void getDescription() {
	}

	@Test
	void getRecipes() {
	}
}