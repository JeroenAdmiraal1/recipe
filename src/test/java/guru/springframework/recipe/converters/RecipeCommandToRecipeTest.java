package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.NotesCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Difficulty;
import guru.springframework.recipe.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeTest {

	public static final Long RECIPE_ID = 1L;
	public static final Integer COOK_TIME = Integer.valueOf("5");
	public static final Integer PREP_TIME = Integer.valueOf("7");
	public static final String DESCRIPTION = "My Recipe";
	public static final String DIRECTIONS = "Directions";
	public static final Difficulty DIFFICULTY = Difficulty.EASY;
	public static final Integer SERVINGS = Integer.valueOf("3");
	public static final String SOURCE = "Source";
	public static final String URL = "Some URL";
	public static final Long CAT_ID_1 = 1L;
	public static final Long CAT_ID2 = 2L;
	public static final Long INGRED_ID_1 = 3L;
	public static final Long INGRED_ID_2 = 4L;
	public static final Long NOTES_ID = 9L;
	public static final Byte[] IMAGE = new Byte[10];

	RecipeCommandToRecipe converter;


	@BeforeEach
	public void setUp() throws Exception {
		converter = new RecipeCommandToRecipe(new NotesCommandToNotes(),
				new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
				new CategoryCommandToCategory());
	}

	@Test
	public void testNullObject() throws Exception {
		assertNull(converter.convert(null));
	}

	@Test
	public void testEmptyObject() throws Exception {
		assertNotNull(converter.convert(new RecipeCommand()));
	}

	@Test
	public void convert() throws Exception {
		//given
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(RECIPE_ID);
		recipeCommand.setCookTime(COOK_TIME);
		recipeCommand.setPrepTime(PREP_TIME);
		recipeCommand.setDescription(DESCRIPTION);
		recipeCommand.setDifficulty(DIFFICULTY);
		recipeCommand.setDirections(DIRECTIONS);
		recipeCommand.setServings(SERVINGS);
		recipeCommand.setSource(SOURCE);
		recipeCommand.setUrl(URL);
		recipeCommand.setImage(IMAGE);

		NotesCommand notes = new NotesCommand();
		notes.setId(NOTES_ID);
		recipeCommand.setNotes(notes);

		CategoryCommand category = new CategoryCommand();
		category.setId(CAT_ID_1);
		CategoryCommand category2 = new CategoryCommand();
		category2.setId(CAT_ID2);
		recipeCommand.getCategories().add(category);
		recipeCommand.getCategories().add(category2);

		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(INGRED_ID_1);
		IngredientCommand ingredient2 = new IngredientCommand();
		ingredient2.setId(INGRED_ID_2);
		recipeCommand.getIngredients().add(ingredient);
		recipeCommand.getIngredients().add(ingredient2);

		//when
		Recipe recipe  = converter.convert(recipeCommand);

		assertNotNull(recipe);
		assertEquals(RECIPE_ID, recipe.getId());
		assertEquals(COOK_TIME, recipe.getCookTime());
		assertEquals(PREP_TIME, recipe.getPrepTime());
		assertEquals(DESCRIPTION, recipe.getDescription());
		assertEquals(DIFFICULTY, recipe.getDifficulty());
		assertEquals(DIRECTIONS, recipe.getDirections());
		assertEquals(SERVINGS, recipe.getServings());
		assertEquals(SOURCE, recipe.getSource());
		assertEquals(URL, recipe.getUrl());
		assertEquals(NOTES_ID, recipe.getNotes().getId());
		assertEquals(IMAGE, recipe.getImage());
		assertEquals(2, recipe.getCategories().size());
		assertEquals(2, recipe.getIngredients().size());
	}

}