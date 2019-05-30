package pl.wroc.pwr.judy.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.general.ICoverage;
import pl.wroc.pwr.judy.general.JUnitTestResult;
import pl.wroc.pwr.judy.general.MapCoverage;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class TargetClassComparatorTest {

	private static final String TEST_COVERAGE_2 = "TC2";
	private static final String TEST_COVERAGE_1 = "TC1";
	private static final String CLASS_NAME_2 = "class2";
	private static final String CLASS_NAME_1 = "class1";
	private TargetClassComparator comparator;
	private IBytecodeCache cache;
	private IInitialTestsRun tests;
	private byte[] classBytecode1;
	private byte[] classBytecode2;

	@Before
	public void setUp() {
		cache = Mockito.mock(IBytecodeCache.class);
		tests = Mockito.mock(IInitialTestsRun.class);
		comparator = new TargetClassComparator(cache, tests);

		classBytecode1 = new byte[]{1, 2, 3, 4, 5, 6};
		classBytecode2 = new byte[]{1, 2, 3};
	}

	@Test
	public void shouldCompareSameBytecodeSizesAndSameDurations() throws Exception {
		ICoverage coverage = new MapCoverage();
		coverage.addClass(CLASS_NAME_1, TEST_COVERAGE_1);

		Mockito.when(cache.get(CLASS_NAME_1)).thenReturn(classBytecode1);
		Mockito.when(tests.getCoverage()).thenReturn(coverage);

		ITestResult tr1 = new JUnitTestResult(TEST_COVERAGE_1, true, 100, null, null);
		Mockito.when(tests.getPassingResults()).thenReturn(Arrays.asList(tr1));

		assertTrue(comparator.compare(CLASS_NAME_1, CLASS_NAME_1) == 0);
	}

	@Test
	public void shouldCompareDifferentBytecodeSizes() throws Exception {
		ICoverage coverage = new MapCoverage();
		coverage.addClass(CLASS_NAME_1, TEST_COVERAGE_1);
		coverage.addClass(CLASS_NAME_2, TEST_COVERAGE_2);

		Mockito.when(cache.get(CLASS_NAME_1)).thenReturn(classBytecode1);
		Mockito.when(cache.get(CLASS_NAME_2)).thenReturn(classBytecode2);
		Mockito.when(tests.getCoverage()).thenReturn(coverage);

		ITestResult tr1 = new JUnitTestResult(TEST_COVERAGE_1, true, 100, null, null);
		Mockito.when(tests.getPassingResults()).thenReturn(Arrays.asList(tr1));

		assertTrue(comparator.compare(CLASS_NAME_1, CLASS_NAME_2) < 0);
		assertTrue(comparator.compare(CLASS_NAME_2, CLASS_NAME_1) > 0);
	}

	@Test
	public void shouldCompareDifferentDurations() throws Exception {
		ICoverage coverage = new MapCoverage();
		coverage.addClass(CLASS_NAME_1, TEST_COVERAGE_1);
		coverage.addClass(CLASS_NAME_1, TEST_COVERAGE_2);
		coverage.addClass(CLASS_NAME_2, TEST_COVERAGE_2);

		Mockito.when(cache.get(CLASS_NAME_1)).thenReturn(classBytecode1);
		Mockito.when(cache.get(CLASS_NAME_2)).thenReturn(classBytecode1);
		Mockito.when(tests.getCoverage()).thenReturn(coverage);

		ITestResult tr1 = new JUnitTestResult(TEST_COVERAGE_1, true, 100, null, null);
		ITestResult tr2 = new JUnitTestResult(TEST_COVERAGE_2, true, 200, null, null);
		ITestResult tr3 = new JUnitTestResult("TC3", true, 500, null, null);
		Mockito.when(tests.getPassingResults()).thenReturn(Arrays.asList(tr1, tr2, tr3));

		assertTrue(comparator.compare(CLASS_NAME_1, CLASS_NAME_2) < 0);
		assertTrue(comparator.compare(CLASS_NAME_2, CLASS_NAME_1) > 0);
	}

}
