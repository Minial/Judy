package pl.wroc.pwr.judy.general;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JUnitTestResultTest {

	private static final Class<?> CLAZZ_A = JUnitTestResult.class;
	private static final Class<?> CLAZZ_B = JUnitTestResultTest.class;
	private static final String METHOD_A = "aaa";
	private static final String METHOD_B = "bbb";

	@Test
	public void shouldInitializeTestResult() throws Exception {
		String name = "testClass";
		boolean passed = false;
		int duration = 1337;

		JUnitTestResult cut = new JUnitTestResult(name, passed, duration, null, null);
		assertEquals(name, cut.getClassName());
		assertEquals(passed, cut.passed());
		assertEquals(duration, cut.getDuration());
	}

	@Test
	public void shouldReturnThrownExceptions() throws Exception {
		Throwable exA = new NullPointerException();
		Throwable exB = new IndexOutOfBoundsException();

		List<Throwable> exceptions = Arrays.asList(exA);
		List<Failure> failures = Arrays.asList(prepareFailure(exB, "testMethod", JUnitTestResult.class));

		TestCaseCollector collector = mockCollector(failures);

		DetailedTestResult details = new DetailedTestResult();
		details.setCollector(collector);

		JUnitTestResult cut = new JUnitTestResult(null, false, 0, exceptions, details);

		assertEquals(Arrays.asList(exB, exA), cut.getThrownExceptions());
	}

	@Test
	public void shouldReturnTestMethods() throws Exception {
		DetailedTestResult details = new DetailedTestResult();
		TestCaseCollector collector = new TestCaseCollector();

		collector.getTestcases().add(Description.createTestDescription(CLAZZ_A, METHOD_A));
		collector.getTestcases().add(Description.createTestDescription(CLAZZ_B, METHOD_A));
		collector.getTestcases().add(Description.createTestDescription(CLAZZ_A, METHOD_B));
		collector.getTestcases().add(Description.createTestDescription(CLAZZ_A, METHOD_B));

		details.setCollector(collector);
		JUnitTestResult cut = new JUnitTestResult(null, false, 0, null, details);
		Set<String> testMethods = cut.getTestMethods();

		verifyNames(METHOD_A, METHOD_B, CLAZZ_A, CLAZZ_B, testMethods);
	}

	@Test
	public void shouldReturnFailedTestMethods() throws Exception {
		List<Failure> failures = Arrays.asList(prepareFailure(null, METHOD_A, CLAZZ_A),
				prepareFailure(null, METHOD_A, CLAZZ_B), prepareFailure(null, METHOD_B, CLAZZ_A),
				prepareFailure(null, METHOD_B, CLAZZ_A));
		DetailedTestResult details = new DetailedTestResult();
		details.setCollector(mockCollector(failures));

		JUnitTestResult cut = new JUnitTestResult(null, false, 0, null, details);
		Set<String> failedMethods = cut.getFailingTestMethods();
		verifyNames(METHOD_A, METHOD_B, CLAZZ_A, CLAZZ_B, failedMethods);
	}

	@Test
	public void shouldReturnSuccessfulMethods() throws Exception {
		List<Failure> failures = Arrays.asList(prepareFailure(null, METHOD_A, CLAZZ_A));

		List<Description> testcases = Arrays.asList(Description.createTestDescription(CLAZZ_A, METHOD_A),
				Description.createTestDescription(CLAZZ_B, METHOD_A),
				Description.createTestDescription(CLAZZ_B, METHOD_A),
				Description.createTestDescription(CLAZZ_A, METHOD_B),
				Description.createTestDescription(CLAZZ_B, METHOD_B));

		DetailedTestResult details = new DetailedTestResult();
		TestCaseCollector collector = mockCollector(failures);
		when(collector.getTestcases()).thenReturn(testcases);
		details.setCollector(collector);

		JUnitTestResult cut = new JUnitTestResult(null, false, 0, null, details);

		Set<String> successfulMethods = cut.getSuccessfulTestMethods();
		verifyNames(METHOD_A, METHOD_B, CLAZZ_A, CLAZZ_B, successfulMethods);
	}

	private void verifyNames(String methodA, String methodB, Class<?> clazzA, Class<?> clazzB, Set<String> methodNames) {
		assertEquals(3, methodNames.size());
		assertTrue(methodNames.contains(createMethodName(methodA, clazzB)));
		assertTrue(methodNames.contains(createMethodName(methodA, clazzB)));
		assertTrue(methodNames.contains(createMethodName(methodB, clazzA)));
	}

	private String createMethodName(String method, Class<?> clazz) {
		return clazz.getName() + "." + method;
	}

	private Failure prepareFailure(Throwable throwable, String methodName, Class<?> clazz) {
		return new Failure(Description.createTestDescription(clazz, methodName), throwable);
	}

	private TestCaseCollector mockCollector(List<Failure> failures) {
		TestCaseCollector collector = mock(TestCaseCollector.class);
		when(collector.getFailures()).thenReturn(failures);
		return collector;
	}
}
