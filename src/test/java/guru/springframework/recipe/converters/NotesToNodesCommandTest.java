package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNodesCommandTest {

	public static final Long ID_VALUE = 1L;
	public static final String RECIPE_NOTES = "Notes";
	NotesToNodesCommand converter;

	@BeforeEach
	public void setUp() throws Exception {
		converter = new NotesToNodesCommand();
	}

	@Test
	public void testNullParameter() throws Exception {
		assertNull(converter.convert(null));
	}

	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new Notes()));
	}

	@Test
	public void convert() throws Exception {
		//given
		Notes notes = new Notes();
		notes.setId(ID_VALUE);
		notes.setRecipeNotes(RECIPE_NOTES);

		//when
		NotesCommand notesCommand = converter.convert(notes);

		//then
		assertNotNull(notesCommand);
		assertEquals(ID_VALUE, notesCommand.getId());
		assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
	}

}