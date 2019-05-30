package pl.wroc.pwr.judy.tester;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.general.TestThread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.*;

public class JUnitMutationTesterTest extends AbstractJUnitTesterTest {

	private static final String TEST_CLASS_NAME = "java.lang.Object";
	private static final String TEST_METHOD1 = "testMethod1";
	private static final String TEST_METHOD2 = "testMethod2";
	private JUnitMutationTester tester;
	private ITestThreadFactory threadFactory;
	private Set<String> testMethods;
	private ThreadPoolExecutor executor;
	private TestThread thread;

	@Before
	public void setUp() throws Exception {

		testMethods = new HashSet<>();
		testMethods.add(TEST_METHOD1);
		testMethods.add(TEST_METHOD2);

		thread = Mockito.mock(TestThread.class);

		executor = Mockito.mock(ThreadPoolExecutor.class);
		threadFactory = mockThreadFactory(thread);
		tester = mockExecutorInitialization(executor, threadFactory);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void shouldSaveExecutionException() throws Exception {
		Future task1 = mockTaskWithException(ExecutionException.class, JUnitTester.TEST_TIMEOUT);
		Mockito.when(executor.submit(Matchers.any(Callable.class))).thenReturn(task1);

		runTestsAndCheckThrownExceptions(ExecutionException.class);

		verifyTaskExecution(task1, thread, executor, JUnitTester.TEST_TIMEOUT);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void shouldGetTestResults() throws Exception {
		Future task1 = Mockito.mock(Future.class);
		Mockito.when(executor.submit(Matchers.any(Callable.class))).thenReturn(task1);

		ITestResult testResults = tester.getTestResult(TEST_CLASS_NAME, testMethods, JUnitTester.TEST_TIMEOUT);

		checkIfResultsPassedWithoutExceptions(testResults);
		verifyTaskExecution(task1, thread, executor, JUnitTester.TEST_TIMEOUT);
	}

	@Test
	public void shouldSaveUnexpectedException() throws Exception {
		tester = new JUnitMutationTester(threadFactory, null);
		runTestsAndCheckThrownExceptions(NullPointerException.class);
	}

	private JUnitMutationTester mockExecutorInitialization(final ThreadPoolExecutor executor,
														   ITestThreadFactory threadFactory) {
		return new JUnitMutationTester(threadFactory, executor);
	}

	private void checkIfResultsPassedWithoutExceptions(ITestResult testResults) {
		assertTrue(testResults.passed());
		assertTrue(testResults.getThrownExceptions().isEmpty());
	}

	private void runTestsAndCheckThrownExceptions(Class<? extends Throwable> exception) throws Exception {
		ITestResult testResults = tester.getTestResult(TEST_CLASS_NAME, testMethods, JUnitTester.TEST_TIMEOUT);

		assertNotNull(testResults);
		assertFalse(testResults.getThrownExceptions().isEmpty());
		assertEquals(exception, testResults.getThrownExceptions().get(0).getClass());
		assertFalse(testResults.passed());
	}

	@SuppressWarnings("unchecked")
	private ITestThreadFactory mockThreadFactory(TestThread thread) {
		ITestThreadFactory factory = Mockito.mock(ITestThreadFactory.class);
		Mockito.when(
				factory.createMutationTestThread(Matchers.any(DetailedTestResult.class), (Class<?>) Matchers.any(),
						Matchers.anySet())).thenReturn(thread);
		return factory;
	}
}
