package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.general.ICoverage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EffortEstimatorTest {

	private static final String CLASS_B = "B";
	private static final String CLASS_A = "A";
	private static final int TESTS = 20;
	private EffortEstimator estimator;
	private List<String> targetClasses;
	private ICoverage coverage;

	@Before
	public void setUp() throws Exception {
		targetClasses = prepareTargetClasses();
		coverage = mockCoverage();
		estimator = new EffortEstimator(TESTS, targetClasses, coverage);
	}

	@Test
	public void shouldReturnEffortFactor() throws Exception {
		assertEquals(133.333, estimator.getEffortFactor(), 0.001);
	}

	@Test
	public void shouldEstimateForKnownClasses() throws Exception {
		assertEquals(66.666, estimator.estimate(CLASS_A), 0.001);
		assertEquals(33.333, estimator.estimate(CLASS_B), 0.001);
	}

	public List<String> prepareTargetClasses() {
		List<String> cls = new ArrayList<>();
		cls.add(CLASS_A);
		cls.add(CLASS_B);
		return cls;
	}

	private ICoverage mockCoverage() {
		ICoverage c = Mockito.mock(ICoverage.class);
		Mockito.when(c.countCoveringTestClasses(CLASS_A)).thenReturn(10);
		Mockito.when(c.countCoveringTestClasses(CLASS_B)).thenReturn(5);
		return c;
	}

}
