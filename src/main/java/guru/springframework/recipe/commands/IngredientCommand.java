package guru.springframework.recipe.commands;

import guru.springframework.recipe.domain.UnitOfMeasure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {

	private Long id;
	private String description;
	private BigDecimal amount;
	private RecipeCommand recipe;
	private UnitOfMeasureCommand unitOfMeasure;
}
