package pl.wroc.pwr.judy.hom.objectives;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class MutationOrderObjectiveTest extends ObjectiveCalculatorTest {

	private MutationOrderObjective calculator;

	@Before
	public void setUp() throws Exception {
		calculator = new MutationOrderObjective(DESCRIPTION);
	}

	@Test
	public void shouldReturn15() throws Exception {
		checkCalculatedValue(15, prepareMutantMock(15), calculator);
	}

	@Test
	public void shouldReturnWorstValue() throws Exception {
		checkCalculatedValue(ObjectiveCalculator.WORST_POSITIVE_VALUE, prepareMutantMock(0), calculator);
		checkCalculatedValue(ObjectiveCalculator.WORST_POSITIVE_VALUE, prepareMutantMock(1), calculator);
	}

	@Test
	public void shouldReckognizeEverySolutionAsHighQuality() throws Exception {
		assertTrue(calculator.isValuable(0.5));
		assertTrue(calculator.isValuable(0.0));
		assertTrue(calculator.isValuable(1.0));
		assertTrue(calculator.isValuable(5.0));
		assertTrue(calculator.isValuable(-1.0));
	}

	private IMutant prepareMutantMock(int order) {
		IMutant hom = Mockito.mock(IMutant.class);
		Mockito.when(hom.getOrder()).thenReturn(order);
		return hom;
	}

	protected IMutant prepareMutantMock(List<String> names) {
		IMutant hom = Mockito.mock(IMutant.class);
		Mockito.when(hom.getOperatorsNames()).thenReturn(names);
		return hom;
	}
}
