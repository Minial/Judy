package pl.wroc.pwr.judy.hom.objectives;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FitnessObjectiveTest extends ObjectiveCalculatorTest {
	private FitnessObjective calculator;
	private FragilityCalculator fc;

	private IMutant hom;
	private List<IMutant> foms;

	@Before
	public void setUp() {
		fc = Mockito.mock(FragilityCalculator.class);
		hom = Mockito.mock(IMutant.class);
		foms = new ArrayList<>();
		calculator = new FitnessObjective(DESCRIPTION, fc);
	}

	@Test
	public void shouldReturnInvalidValue() throws Exception {
		assertEquals(Integer.MAX_VALUE, calculator.getWorstValue(), PRECISION);
	}

	@Test
	public void shouldReturnDescription() throws Exception {
		assertEquals(DESCRIPTION, calculator.getDescription());
	}

	@Test
	public void shouldReturnZeroAsFitnessValueForZeroFomsFragility() throws Exception {
		mockFragility(0.0, 0.5);
		assertEquals(0.0, calculator.calculate(hom, foms, foms.size()), PRECISION);
	}

	@Test
	public void shouldReturnValidFitnessValue() throws Exception {
		mockFragility(0.25, 0.33333);
		assertEquals(1.3333, calculator.calculate(hom, foms, foms.size()), PRECISION);
	}

	private void mockFragility(double fomFragility, double homFragility) {
		Mockito.when(fc.getFragility(foms)).thenReturn(fomFragility);
		Mockito.when(fc.getFragility(hom)).thenReturn(homFragility);
	}

	@Test
	public void shouldReckognizeHighQualityValue() throws Exception {
		assertTrue(calculator.isValuable(0.5));
	}

	@Test
	public void shouldReckognizeNoHighQualityValue() throws Exception {
		assertFalse(calculator.isValuable(0.0));
		assertFalse(calculator.isValuable(1.0));
		assertFalse(calculator.isValuable(5.0));
		assertFalse(calculator.isValuable(-1.0));
	}
}
