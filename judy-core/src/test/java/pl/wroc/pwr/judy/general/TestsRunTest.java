package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestsRunTest {
	private static final int DURATION = 100;
	private List<ITestResult> results;
	private Map<String, String> inheritance;
	private ICoverage coverage;

	private TestsRun testsRun;

	@Before
	public void setUp() {
		inheritance = new HashMap<>();
		inheritance.put("x", "y");

		coverage = Mockito.mock(ICoverage.class);

		results = new ArrayList<>();
		testsRun = new TestsRun(results, inheritance, coverage, DURATION);
	}

	@Test
	public void shouldBeMarkedAsPassed() throws Exception {
		prepareTestResults(true, true);
		assertTrue(testsRun.passed());
	}

	@Test
	public void shouldBeMarkedAsFailed() throws Exception {
		prepareTestResults(true, false);
		assertFalse(testsRun.passed());
	}

	@Test
	public void shouldReturnPassingResultsOnly() throws Exception {
		prepareTestResults(true, false);
		assertEquals(1, testsRun.getPassingResults().size());
	}

	@Test
	public void shouldReturnEmptyListForFailedResultsOnly() throws Exception {
		prepareTestResults(false, false);
		assertTrue(testsRun.getPassingResults().isEmpty());
	}

	@Test
	public void shouldReturnInheritance() throws Exception {
		assertEquals(inheritance, testsRun.getInheritance());
	}

	@Test
	public void shouldReturnCoverage() throws Exception {
		assertEquals(coverage, testsRun.getCoverage());
	}

	@Test
	public void shouldReturnDuration() throws Exception {
		assertEquals(DURATION, testsRun.getDuration());
	}

	private void prepareTestResults(boolean passed1, boolean passed2) {
		results.add(mockTestResult(passed1));
		results.add(mockTestResult(passed2));
	}

	private ITestResult mockTestResult(boolean passed) {
		ITestResult result = Mockito.mock(ITestResult.class);
		Mockito.when(result.passed()).thenReturn(passed);
		return result;
	}
}
