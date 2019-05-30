package pl.wroc.pwr.judy.hom.objectives;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MaxOperatorDiversityObjectiveTest extends ObjectiveCalculatorTest {

	public ObjectiveCalculator calculator;

	@Before
	public void setUp() throws Exception {
		calculator = new MaxOperatorDiversityObjective(DESCRIPTION);
	}

	@Test
	public void shouldReturnZero() throws Exception {
		IMutant hom = prepareMutantMock(new ArrayList<String>());
		checkCalculatedValue(0.0, hom, calculator);
	}

	@Test
	public void shouldReturnOne() throws Exception {
		List<String> names = Arrays.asList("AOR_Add");
		IMutant hom = prepareMutantMock(names);
		checkCalculatedValue(-1.0, hom, calculator);
	}

	@Test
	public void shouldReturnQuarter() throws Exception {
		List<String> names = Arrays.asList("AOR_Add", "AOR_Rem", "AOR_Add", "AOR_Add");
		IMutant hom = prepareMutantMock(names);
		checkCalculatedValue(-0.5, hom, calculator);
	}

	@Test
	public void shouldReturnInvalidValue() throws Exception {
		assertEquals(Integer.MAX_VALUE, calculator.getWorstValue(), PRECISION);
	}

	@Test
	public void shouldReturnDescription() throws Exception {
		assertEquals(DESCRIPTION, calculator.getDescription());
	}

	private IMutant prepareMutantMock(List<String> names) {
		IMutant hom = Mockito.mock(IMutant.class);
		Mockito.when(hom.getOperatorsNames()).thenReturn(names);
		return hom;
	}

	@Test
	public void shouldReckognizeEverySolutionAsHighQuality() throws Exception {
		assertTrue(calculator.isValuable(0.5));
		assertTrue(calculator.isValuable(0.0));
		assertTrue(calculator.isValuable(1.0));
		assertTrue(calculator.isValuable(5.0));
		assertTrue(calculator.isValuable(-1.0));
	}
}
