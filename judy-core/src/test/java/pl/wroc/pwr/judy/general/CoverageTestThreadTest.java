package pl.wroc.pwr.judy.general;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.loader.TestClassLoader;

import static org.junit.Assert.*;

public class CoverageTestThreadTest {
	private static final String TEST_METHOD = "testMethod";
	private static final String TEST_CLASS_NAME = "testClassName";
	private CoverageTestThread thread;
	private DetailedTestResult detailedResult;
	private ICoverageClassLoaderFactory classLoaderFactory;
	private JUnitCore runner;
	private TestClassLoader classLoader;
	protected IRuntime runtime;
	protected Instrumenter instrumenter;
	protected Result result;
	protected CoverageAnalyzer analyzer;
	private ExecutionDataStore executionData;

	@Before
	public void setUp() throws Exception {
		runtime = Mockito.mock(IRuntime.class);
		instrumenter = Mockito.mock(Instrumenter.class);
		result = Mockito.mock(Result.class);
		analyzer = Mockito.mock(CoverageAnalyzer.class);

		detailedResult = new DetailedTestResult();
		classLoader = mockClassLoader();
		classLoaderFactory = mockClassLoaderFactory(classLoader, instrumenter);

		runner = mockTestRunAndAddingListener();
		thread = mockInitializations();
	}

	@Test
	public void shouldNotRunAnalysisForFailedTest() throws Exception {
		Mockito.when(result.wasSuccessful()).thenReturn(false);

		thread.call();

		assertEquals(1, detailedResult.getTestMethods().size());
		Mockito.verify(runtime).startup((RuntimeData) Matchers.any());
		Mockito.verify(runtime).shutdown();
		Mockito.verify(classLoaderFactory).createLoader(instrumenter);
	}

	@Test
	public void shouldRunAnalysisForSuccessfulTest() throws Exception {
		Mockito.when(result.wasSuccessful()).thenReturn(true);

		thread.call();

		assertEquals(1, detailedResult.getTestMethods().size());
		Mockito.verify(runtime).startup((RuntimeData) Matchers.any());
		Mockito.verify(classLoaderFactory).createLoader(instrumenter);
		Mockito.verify(analyzer).analyze(TEST_CLASS_NAME, TEST_METHOD, executionData);
		Mockito.verify(runtime).shutdown();
	}

	@Test
	public void shouldHandleRuntimeInitializationException() throws Exception {
		Mockito.doThrow(new Exception()).when(runtime).startup((RuntimeData) Matchers.any());
		thread.call();

		assertEquals(0, detailedResult.getTestMethods().size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldHandleClassNotFoundException() throws Exception {
		Mockito.when(classLoader.loadClass(TEST_CLASS_NAME)).thenThrow(ClassNotFoundException.class);
		thread.call();

		assertTrue(detailedResult.getTestMethods().isEmpty());
		assertTrue(detailedResult.getFailures().isEmpty());
	}

	private JUnitCore mockTestRunAndAddingListener() {
		return new JUnitCore() {
			@Override
			public Result run(Request request) {
				try {
					detailedResult.getCollector().testStarted(Mockito.mock(Description.class));
				} catch (Exception e) {
				}
				return result;
			}

			@Override
			public void addListener(RunListener listener) {
				super.addListener(listener);
				assertEquals(detailedResult.getCollector(), listener);
			}
		};
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private TestClassLoader mockClassLoader() throws ClassNotFoundException {
		TestClassLoader classLoader = Mockito.mock(TestClassLoader.class);
		Class clazz = TestThreadTest.class;
		Mockito.when(classLoader.loadClass(TEST_CLASS_NAME)).thenReturn(clazz);
		return classLoader;
	}

	private ICoverageClassLoaderFactory mockClassLoaderFactory(TestClassLoader classLoader, Instrumenter instrumenter) {
		ICoverageClassLoaderFactory factory = Mockito.mock(ICoverageClassLoaderFactory.class);
		Mockito.when(factory.createLoader()).thenReturn(classLoader);
		Mockito.when(factory.createLoader(instrumenter)).thenReturn(classLoader);

		return factory;
	}

	private CoverageTestThread mockInitializations() {
		return new CoverageTestThread(TEST_CLASS_NAME, TEST_METHOD, detailedResult, classLoaderFactory) {
			@Override
			public JUnitCore createRunner() {
				return runner;
			}

			@Override
			protected IRuntime createRuntime() {
				return runtime;
			}

			@Override
			protected Instrumenter createInstrumenter(IRuntime runtime) {
				return instrumenter;
			}

			@Override
			protected CoverageAnalyzer createAnalyzer(ExecutionDataStore executionData, TestClassLoader loader) {
				CoverageTestThreadTest.this.executionData = executionData;
				return analyzer;
			}
		};
	}

	@Test
	public void testCreateAnalyzer() throws Exception {
		thread = new CoverageTestThread(TEST_CLASS_NAME, TEST_METHOD, detailedResult, classLoaderFactory);
		CoverageAnalyzer analyzer = thread.createAnalyzer(null, classLoader);
		assertNotNull(analyzer);
	}

	@Test
	public void testCreateInstrumenter() throws Exception {
		thread = new CoverageTestThread(TEST_CLASS_NAME, TEST_METHOD, detailedResult, classLoaderFactory);
		Instrumenter instrumenter = thread.createInstrumenter(runtime);
		assertNotNull(instrumenter);

	}

	@Test
	public void testCreateRuntime() throws Exception {
		thread = new CoverageTestThread(TEST_CLASS_NAME, TEST_METHOD, detailedResult, classLoaderFactory);
		IRuntime result = thread.createRuntime();
		assertNotNull(result);
		assertTrue(result instanceof LoggerRuntime);
	}
}
