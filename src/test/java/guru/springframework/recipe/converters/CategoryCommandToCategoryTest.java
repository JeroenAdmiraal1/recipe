package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

	public static final String DESCRIPTION = "description";
	public static final Long LONG_VALUE = new Long(1L);

	CategoryCommandToCategory converter;

	@BeforeEach
	void setUp() {
		converter = new CategoryCommandToCategory();
	}

	@Test
	void testNullParameter(){
		assertNull(converter.convert(null));
	}

	@Test
	void testEmptyObject(){
		assertNotNull(converter.convert(new CategoryCommand()));
	}

	@Test
	void convert() {
		//given
		CategoryCommand command = new CategoryCommand();
		command.setId(LONG_VALUE);
		command.setDescription(DESCRIPTION);

		//when
		Category category = converter.convert(command);

		//then
		assertNotNull(category);
		assertEquals(LONG_VALUE, category.getId());
		assertEquals(DESCRIPTION, category.getDescription());
	}

}