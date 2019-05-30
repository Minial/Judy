package pl.wroc.pwr.judy.tester;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.general.CoverageTestThread;
import pl.wroc.pwr.judy.general.DetailedTestResult;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.loader.TestClassLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class JUnitCoverageTesterTest extends AbstractJUnitTesterTest {

	private static final int THREADS_COUNT = 10;
	private static final String TEST_CLASS_NAME = "testClassName";
	private static final String TEST_METHOD1 = "testMethod1";
	private static final String TEST_METHOD2 = "testMethod2";
	private JUnitCoverageTester tester;
	private ITestThreadFactory threadFactory;
	private Set<String> testMethods;
	private TestClassLoader classLoader;
	private ICoverageClassLoaderFactory factory;
	private ThreadPoolExecutor executor;
	private CoverageTestThread thread;

	@Before
	public void setUp() throws Exception {

		testMethods = new HashSet<>();
		testMethods.add(TEST_METHOD1);
		testMethods.add(TEST_METHOD2);

		classLoader = Mockito.mock(TestClassLoader.class);
		factory = mockClassLoaderFactory(classLoader);

		thread = Mockito.mock(CoverageTestThread.class);

		executor = Mockito.mock(ThreadPoolExecutor.class);
		threadFactory = mockThreadFactory(thread);
		tester = mockExecutorInitialization(executor, threadFactory);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void shouldSaveExecutionException() throws Exception {
		Future task1 = Mockito.mock(Future.class);
		Future task2 = mockTaskWithException(ExecutionException.class, JUnitTester.TEST_TIMEOUT);
		Mockito.when(executor.submit(Matchers.any(Callable.class))).thenReturn(task1, task2);

		runTestsAndCheckThrownExceptions(ExecutionException.class);

		verifyTasksExecution(task1, task2, thread, executor, JUnitTester.TEST_TIMEOUT);
	}

	@Test
	public void shouldSaveUnexpectedException() throws Exception {
		tester = new JUnitCoverageTester(factory, threadFactory, null) {
			@Override
			public ExecutorService getExecutor() {
				throw new NullPointerException();
			}
		};

		runTestsAndCheckThrownExceptions(NullPointerException.class);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void shouldGetTestResults() throws Exception {
		Future task1 = Mockito.mock(Future.class);
		Future task2 = Mockito.mock(Future.class);
		Mockito.when(executor.submit(Matchers.any(Callable.class))).thenReturn(task1, task2);

		ITestResult testResults = tester.getTestResult(TEST_CLASS_NAME, testMethods, JUnitTester.TEST_TIMEOUT);

		checkIfResultsPassedWithoutExceptions(testResults);
		verifyTasksExecution(task1, task2, thread, executor, JUnitTester.TEST_TIMEOUT);
	}

	private JUnitCoverageTester mockExecutorInitialization(final ThreadPoolExecutor executor,
														   ITestThreadFactory threadFactory) {
		return new JUnitCoverageTester(factory, threadFactory, executor);
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

	private ITestThreadFactory mockThreadFactory(CoverageTestThread thread) {
		ITestThreadFactory factory = Mockito.mock(ITestThreadFactory.class);
		Mockito.when(
				factory.createCoverageTestThread(Matchers.any(ICoverageClassLoaderFactory.class),
						Matchers.any(DetailedTestResult.class), Matchers.anyString(), Matchers.anyString()))
				.thenReturn(thread);
		return factory;
	}

	private ICoverageClassLoaderFactory mockClassLoaderFactory(TestClassLoader classLoader) {
		ICoverageClassLoaderFactory factory = Mockito.mock(ICoverageClassLoaderFactory.class);
		Mockito.when(factory.createLoader()).thenReturn(classLoader);
		return factory;
	}
}
