package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MapCoverageTest {

	private static final String SOURCE_CLASS_NAME = "sourceClassName";
	private static final String UNCOVERED_SOURCE_CLASS_NAME = "uncoveredSourceClassName";

	private static final String TEST_CLASS_NAME_1 = "testClassName";
	private static final String TEST_CLASS_NAME_2 = "testClassName2";

	private static final String TEST_METHOD_NAME = "testMethodName";
	private MapCoverage coverage;
	private TestCoverage testCoverage;

	@Before
	public void setUp() throws Exception {
		coverage = new MapCoverage();
		initData();
	}

	@Test
	public void shouldAddNewCoveredClass() throws Exception {
		coverage = new MapCoverage();
		coverage.addClass(SOURCE_CLASS_NAME, TEST_CLASS_NAME_1);
		assertEquals(1, coverage.getCoverageMap().size());
	}

	@Test
	public void shouldAddTestClassToAlreadyKnownSourceClass() throws Exception {
		assertEquals(2, coverage.getCoverageMap().get(SOURCE_CLASS_NAME).size());

		coverage.addClass(SOURCE_CLASS_NAME, "newTestClassName");

		assertEquals(3, coverage.getCoverageMap().get(SOURCE_CLASS_NAME).size());
	}

	@Test
	public void shouldAddMethodForKnownTestClass() throws Exception {
		coverage.addMethod(SOURCE_CLASS_NAME, TEST_CLASS_NAME_1, new MethodCoverage(TEST_METHOD_NAME));
		assertEquals(5, testCoverage.getMethodCoverage().size());
	}

	@Test
	public void shouldNotAddMethodIfTestClassIsUnknown() throws Exception {
		coverage = new MapCoverage();
		assertTrue(coverage.getCoverageMap().isEmpty());
		coverage.addMethod("unknownSourceClassName", TEST_CLASS_NAME_1, new MethodCoverage(TEST_METHOD_NAME));
		assertTrue(coverage.getCoverageMap().isEmpty());
	}

	@Test
	public void shouldGetCoveredSourceClasses() throws Exception {
		Set<String> covered = coverage.getCoveredClasses(TEST_CLASS_NAME_1);
		assertEquals(1, covered.size());
	}

	@Test
	public void shouldCountCoveringTestClasses() throws Exception {
		assertEquals(2, coverage.countCoveringTestClasses(SOURCE_CLASS_NAME));
	}

	@Test
	public void shouldGetCoveringTestClasses() throws Exception {
		assertEquals(2, coverage.getCoveringTestClasses(SOURCE_CLASS_NAME).size());

		Set<String> expectedTestClasses = new HashSet<>();
		expectedTestClasses.add(TEST_CLASS_NAME_1);
		expectedTestClasses.add(TEST_CLASS_NAME_2);

		assertEquals(expectedTestClasses, coverage.getCoveringTestClasses(SOURCE_CLASS_NAME));
	}

	@Test
	public void shouldReturnTrueForCoveredSourceClass() throws Exception {
		assertTrue(coverage.isCovered(SOURCE_CLASS_NAME));
	}

	@Test
	public void shouldReturnFalseForUnknownSourceClass() throws Exception {
		assertFalse(coverage.isCovered("uncoveredSourceClassName"));
	}

	@Test
	public void shouldReturnFalseForUncoveredSourceClass() throws Exception {
		assertFalse(coverage.isCovered("uncoveredSourceClassName"));
	}

	@Test
	public void shouldReturnMethodsCoveringLines() throws Exception {
		List<Integer> lines = Arrays.asList(1, 2, 3, 4, 5);
		Set<String> testMethods = coverage.getCoveringTestMethods(SOURCE_CLASS_NAME, TEST_CLASS_NAME_1, lines);
		assertEquals(3, testMethods.size());
	}

	@Test
	public void shouldReturnEmptyMethodsSetForUncoveredClass() throws Exception {
		List<Integer> lines = Arrays.asList(1, 2, 3, 4, 5);
		Set<String> testMethods = coverage
				.getCoveringTestMethods(UNCOVERED_SOURCE_CLASS_NAME, TEST_CLASS_NAME_1, lines);
		assertTrue(testMethods.isEmpty());
	}

	private void initData() {
		Set<TestCoverage> coverage = new HashSet<>();
		testCoverage = new TestCoverage(TEST_CLASS_NAME_1);
		MethodCoverage method1 = new MethodCoverage("methodName1", Arrays.asList(1, 2));
		MethodCoverage method2 = new MethodCoverage("methodName2", Arrays.asList(5));
		MethodCoverage method3 = new MethodCoverage("methodName3", Arrays.asList(3, 4, 7));
		MethodCoverage method4 = new MethodCoverage("methodName4", Arrays.asList(7, 8, 9));
		testCoverage.addMethod(method1);
		testCoverage.addMethod(method2);
		testCoverage.addMethod(method3);
		testCoverage.addMethod(method4);
		coverage.add(testCoverage);
		coverage.add(new TestCoverage(TEST_CLASS_NAME_2));

		this.coverage.getCoverageMap().put(SOURCE_CLASS_NAME, coverage);
		this.coverage.getCoverageMap().put(UNCOVERED_SOURCE_CLASS_NAME, new HashSet<TestCoverage>());
	}
}
