package guru.springframework.recipe.repositories.reactive;

import guru.springframework.recipe.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {

	Mono<Recipe> findById(String id);
}
