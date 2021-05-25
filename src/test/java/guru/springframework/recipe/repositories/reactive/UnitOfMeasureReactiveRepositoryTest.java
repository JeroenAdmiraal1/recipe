package guru.springframework.recipe.repositories.reactive;

import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureReactiveRepositoryTest {

	public static final String EACH = "Each";

	@Autowired
	UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	@BeforeEach
	public void setUp() throws Exception {
		unitOfMeasureReactiveRepository.deleteAll().block();
	}

	@Test
	public void testSave() throws Exception {
		UnitOfMeasure uom = new UnitOfMeasure();
		uom.setDescription(EACH);

		unitOfMeasureReactiveRepository.save(uom).block();

		Long count = unitOfMeasureReactiveRepository.count().block();

		assertEquals(Long.valueOf(1L), count);

	}

	@Test
	public void testFindByDescription() throws Exception {
		UnitOfMeasure uom = new UnitOfMeasure();
		uom.setDescription(EACH);

		unitOfMeasureReactiveRepository.save(uom).block();

		UnitOfMeasure foundUOM = unitOfMeasureReactiveRepository.findByDescription(EACH).block();

		assertEquals(EACH, foundUOM.getDescription());

	}
}