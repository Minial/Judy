package pl.wroc.pwr.judy.hom.research;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.moeaframework.core.Solution;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HomQualityVerifierTest {
	private static final double VALUE = 0.7;
	private static final double VALUE_2 = 1.5;
	private Solution solution;

	@Before
	public void setUp() {
		solution = new Solution(1, 2);
		solution.setObjective(0, VALUE);
		solution.setObjective(1, VALUE_2);
	}

	@Test
	public void shouldVerifyPositively() throws Exception {
		List<ObjectiveCalculator> objectives = Arrays
				.asList(mockCalculator(VALUE, true), mockCalculator(VALUE_2, true));

		assertTrue(HomQualityVerifier.verify(objectives, solution));
	}

	@Test
	public void shouldVerifyNegatively() throws Exception {
		List<ObjectiveCalculator> objectives = Arrays.asList(mockCalculator(VALUE, true),
				mockCalculator(VALUE_2, false));
		assertFalse(HomQualityVerifier.verify(objectives, solution));
	}

	@Test
	public void shouldVerifyNegativelyWithObjectivesAndValuesOfDifferentSize() throws Exception {
		assertFalse(HomQualityVerifier.verify(Arrays.asList(mockCalculator(VALUE, true)), solution));
	}

	@Test
	public void shouldVerifyNegativelyWithEmptyObjectives() throws Exception {
		assertFalse(HomQualityVerifier.verify(new ArrayList<ObjectiveCalculator>(), solution));
	}

	private ObjectiveCalculator mockCalculator(double value, boolean result) {
		ObjectiveCalculator calc = Mockito.mock(ObjectiveCalculator.class);
		Mockito.when(calc.isValuable(value)).thenReturn(result);
		return calc;
	}
}
