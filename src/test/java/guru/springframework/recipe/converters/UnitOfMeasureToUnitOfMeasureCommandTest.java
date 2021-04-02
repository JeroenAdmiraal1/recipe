package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureToUnitOfMeasureCommandTest {

	public static final String DESCRIPTION = "description";
	public static final Long LONG_VALUE = new Long(1L);

	UnitOfMeasureToUnitOfMeasureCommand converter;

	@BeforeEach
	void setUp() {
		converter = new UnitOfMeasureToUnitOfMeasureCommand();
	}

	@Test
	void testNullParameter(){
		assertNull(converter.convert(null));
	}

	@Test
	void testEmptyObject(){
		assertNotNull(converter.convert(new UnitOfMeasure()));
	}

	@Test
	void convert() {
		//given
		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		unitOfMeasure.setId(LONG_VALUE);
		unitOfMeasure.setDescription(DESCRIPTION);

		//when
		UnitOfMeasureCommand unitOfMeasureCommand = converter.convert(unitOfMeasure);

		//then
		assertNotNull(unitOfMeasureCommand);
		assertEquals(LONG_VALUE, unitOfMeasureCommand.getId());
		assertEquals(DESCRIPTION, unitOfMeasureCommand.getDescription());
	}

}