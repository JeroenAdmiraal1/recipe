package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {

	public static final String DESCRIPTION = "description";
	public static final String LONG_VALUE = "1";

	UnitOfMeasureCommandToUnitOfMeasure converter;

	@BeforeEach
	void setUp() {
		converter = new UnitOfMeasureCommandToUnitOfMeasure();
	}

	@Test
	void testNullParameter(){
		assertNull(converter.convert(null));
	}

	@Test
	void testEmptyObject(){
		assertNotNull(converter.convert(new UnitOfMeasureCommand()));
	}

	@Test
	void convert() {
		//given
		UnitOfMeasureCommand command = new UnitOfMeasureCommand();
		command.setId(LONG_VALUE);
		command.setDescription(DESCRIPTION);

		//when
		UnitOfMeasure unitOfMeasure = converter.convert(command);

		//then
		assertNotNull(unitOfMeasure);
		assertEquals(LONG_VALUE, unitOfMeasure.getId());
		assertEquals(DESCRIPTION, unitOfMeasure.getDescription());
	}
}