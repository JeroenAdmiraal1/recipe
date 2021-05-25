package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {

	Flux<UnitOfMeasureCommand> listAllUnitOfMeasure();
}
