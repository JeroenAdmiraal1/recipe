package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {

	public static final String DESCRIPTION = "description";
	public static final Long LONG_VALUE = new Long(1L);

	CategoryToCategoryCommand converter;

	@BeforeEach
	void setUp() {
		converter = new CategoryToCategoryCommand();
	}

	@Test
	void testNullParameter(){
		assertNull(converter.convert(null));
	}

	@Test
	void testEmptyObject(){
		assertNotNull(converter.convert(new Category()));
	}

	@Test
	void convert() {
		//given
		Category category = new Category();
		category.setId(LONG_VALUE);
		category.setDescription(DESCRIPTION);

		//when
		CategoryCommand categoryCommand = converter.convert(category);

		//then
		assertNotNull(categoryCommand);
		assertEquals(LONG_VALUE, categoryCommand.getId());
		assertEquals(DESCRIPTION, categoryCommand.getDescription());
	}

}