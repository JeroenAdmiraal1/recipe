package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import guru.springframework.recipe.repositories.UnitOfMeasureRepository;
import guru.springframework.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

	UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
	UnitOfMeasureService service;

	@Mock
	UnitOfMeasureReactiveRepository unitOfMeasureRepository;

	@BeforeEach
	public void setUp() throws Exception {
		service = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
	}

	@Test
	void listAllUnitOfMeasure() {
		//given
		Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
		UnitOfMeasure uom1 = new UnitOfMeasure();
		uom1.setId("1");
		unitOfMeasures.add(uom1);

		UnitOfMeasure uom2 = new UnitOfMeasure();
		uom2.setId("2");
		unitOfMeasures.add(uom2);

		when(unitOfMeasureRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

		//when
		List<UnitOfMeasureCommand> commands = service.listAllUnitOfMeasure().collectList().block();

		//then
		assertEquals(2, commands.size());
		verify(unitOfMeasureRepository, times(1)).findAll();
	}
}