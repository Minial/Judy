package pl.wroc.pwr.judy.tester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.general.JUnitTestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JUnitTesterTest extends AbstractJUnitTesterTest {
	private static final int DURATION = 0;
	private static final String TEST_CLASS_NAME = "testClassName";
	private JUnitTester tester;

	@Before
	public void setUp() throws Exception {
		tester = createTester(20);
	}

	@Test
	public void shouldCreateRunResultWithDefaultDetailedTestResult() throws Exception {
		JUnitTestResult results = tester.createRunResult(TEST_CLASS_NAME, null, DURATION, new ArrayList<Throwable>());

		checkResultCollectionSizes(results, 0, 0, 0);
		checkBasicResultsData(results, true, 0);
	}

	@Test
	public void shouldCreateFailedRunResultWithDefaultDetailedTestResult() throws Exception {
		JUnitTestResult results = tester.createRunResult(TEST_CLASS_NAME, null, DURATION,
				Arrays.asList(new Throwable()));

		checkResultCollectionSizes(results, 1, 0, 0);
		checkBasicResultsData(results, false, 0);
	}

	@Test
	public void shouldCreateFailedRunResult() throws Exception {
		DetailedTestResult details = Mockito.mock(DetailedTestResult.class);
		Mockito.when(details.getTestMethods()).thenReturn(Arrays.asList(Mockito.mock(Description.class)));
		Mockito.when(details.getFailures()).thenReturn(
				Arrays.asList(new Failure(Mockito.mock(Description.class), new Throwable())));

		JUnitTestResult results = tester
				.createRunResult(TEST_CLASS_NAME, details, DURATION, new ArrayList<Throwable>());

		checkResultCollectionSizes(results, 1, 1, 1);
		checkBasicResultsData(results, false, 0);
	}

	@Test
	public void shouldHandleTestThreadTimeout() throws Exception {
		Future<?> task = checkExceptionHandling(TimeoutException.class);
		Mockito.verify(task).cancel(true);
	}

	@Test
	public void shouldHandleTestThreadInterruption() throws Exception {
		Future<?> task = checkExceptionHandling(InterruptedException.class);
		Mockito.verify(task).get(JUnitTester.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
	}

	@Test
	public void shouldHandleTestThreadExecutionException() throws Exception {
		Future<?> task = checkExceptionHandling(ExecutionException.class);
		Mockito.verify(task).get(JUnitTester.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldHandleTestThreadRunWithoutExceptions() throws Exception {
		Future<Result> validTask = Mockito.mock(Future.class);

		List<TestTask> threads = new ArrayList<>();
		threads.add(new TestTask(TEST_CLASS_NAME, "validTask", validTask));

		List<Exception> result = tester.handleTimeouts(threads, JUnitTester.TEST_TIMEOUT);
		assertTrue(result.isEmpty());

		Mockito.verify(validTask).get(JUnitTester.TEST_TIMEOUT, TimeUnit.MILLISECONDS);
	}

	private void checkResultCollectionSizes(JUnitTestResult results, int expectedExceptionsSize,
											int expectedFailingMethodsSize, int expectedTestMethodsSize) {
		assertEquals(expectedFailingMethodsSize, results.getFailingTestMethods().size());
		assertEquals(expectedTestMethodsSize, results.getTestMethods().size());
		assertEquals(expectedExceptionsSize, results.getThrownExceptions().size());
	}

	private void checkBasicResultsData(JUnitTestResult results, boolean expectedPassed, int expectedDuration) {
		assertEquals(expectedPassed, results.passed());
		assertEquals(TEST_CLASS_NAME, results.getClassName());
		assertEquals(expectedDuration, results.getDuration());
	}

	private Future<?> checkExceptionHandling(Class<? extends Exception> exceptionClazz) throws InterruptedException,
			ExecutionException, TimeoutException {
		Future<Result> interruptedTask = mockTaskWithException(exceptionClazz, JUnitTester.TEST_TIMEOUT);

		List<TestTask> threads = new ArrayList<>();
		threads.add(new TestTask(TEST_CLASS_NAME, "interruptedTask", interruptedTask));

		List<Exception> result = tester.handleTimeouts(threads, JUnitTester.TEST_TIMEOUT);
		assertEquals(1, result.size());
		assertEquals(exceptionClazz, result.get(0).getClass());
		return interruptedTask;
	}

	private JUnitTester createTester(int threadCount) {
		return new JUnitTester(null) {
		};
	}
}
