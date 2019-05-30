package pl.wroc.pwr.judy.general;

import classes.pl.wroc.pwr.Dummy;
import classes.pl.wroc.pwr.DummyTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.MutationException;
import pl.wroc.pwr.judy.loader.ClassLoaderFactory;
import pl.wroc.pwr.judy.loader.ICoverageClassLoaderFactory;
import pl.wroc.pwr.judy.loader.TestClassLoader;
import pl.wroc.pwr.judy.tester.ITester;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.tester.JUnitTester;

import java.util.*;

import static org.junit.Assert.*;

public class InitialTestRunnerTest {

	private static final Class<Dummy> SOURCE_CLASS = Dummy.class;
	private static final Class<DummyTest> TEST_CLASS = DummyTest.class;

	private static final String WORKSPACE = "workspace";

	private List<String> testClasses;
	private List<String> classpaths;
	private ITesterFactory testerFactory;
	private ICoverageClassLoaderFactory coverageFactory;
	private TestClassLoader loader;
	private ITester tester;

	private InitialTestRunner cut;
	private Set<String> testMethods;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		classpaths = new ArrayList<>();
		testClasses = new ArrayList<>();
		testMethods = prepareTestMethods();

		tester = Mockito.mock(ITester.class);
		testerFactory = Mockito.mock(ITesterFactory.class);
		loader = Mockito.mock(TestClassLoader.class);
		coverageFactory = Mockito.mock(ICoverageClassLoaderFactory.class);

		Mockito.when(testerFactory.createCoverageTester(coverageFactory)).thenReturn(tester);
		Mockito.when(coverageFactory.createLoader()).thenReturn(loader);

		cut = new InitialTestRunner(WORKSPACE, testClasses, classpaths, testerFactory) {
			@Override
			protected ICoverageClassLoaderFactory createClassLoaderFactory(Map<String, String> inheritance,
																		   ICoverage coverage, String testClassName) {
				return coverageFactory;
			}
		};
	}

	@Test
	public void shouldReturnCache() throws Exception {
		IBytecodeCache cache = cut.getBytecodeCache();
		assertNotNull(cache);
	}

	@Test(expected = MutationException.class)
	public void shouldFailWithEmptyTestclassesList() throws Exception {
		cut.run();
	}

	@Test
	public void shouldNotRunNotJunitClass() throws Exception {
		testClasses.add(SOURCE_CLASS.getName());
		mockClassLoading(SOURCE_CLASS);

		IInitialTestsRun testsRun = cut.run();
		assertTrue(testsRun.getPassingResults().isEmpty());
	}

	@Test
	public void shouldHandleClassNotFoundExceptionAndReturnEmptyResults() throws Exception {
		testClasses.add(TEST_CLASS.getName());
		mockClassLoadingThrowingException(new ClassNotFoundException());

		IInitialTestsRun testsRun = cut.run();
		assertTrue(testsRun.getPassingResults().isEmpty());
	}

	@Test
	public void shouldHandleNoClassDefFoundErrorAndReturnEmptyResults() throws Exception {
		testClasses.add(TEST_CLASS.getName());
		mockClassLoading(TEST_CLASS);

		mockTestRunningThrowingException(testMethods, new NoClassDefFoundError());

		IInitialTestsRun testsRun = cut.run();
		assertTrue(testsRun.getPassingResults().isEmpty());
	}

	@Test
	public void shouldRunJunitClassTestMethods() throws Exception {
		testClasses.add(TEST_CLASS.getName());
		mockClassLoading(TEST_CLASS);

		ITestResult testResult = mockTestResult(true);
		mockTestRunning(testMethods, testResult);

		IInitialTestsRun testsRun = cut.run();
		assertEquals(1, testsRun.getPassingResults().size());
	}

	@Test
	public void shouldCreateCoverageClassLoaderFactory() throws Exception {
		cut = new InitialTestRunner(WORKSPACE, testClasses, classpaths, testerFactory);
		ClassLoaderFactory factory = cut.createClassLoaderFactory(null, null, TEST_CLASS.getName());
		assertNotNull(factory);
	}

	private ITestResult mockTestResult(boolean passed) {
		ITestResult result = Mockito.mock(ITestResult.class);
		Mockito.when(result.passed()).thenReturn(passed);
		return result;
	}

	private void mockTestRunning(Set<String> testMethods, ITestResult testResult) {
		Mockito.when(tester.getTestResult(TEST_CLASS.getName(), testMethods, JUnitTester.TEST_TIMEOUT)).thenReturn(
				testResult);
	}

	private void mockTestRunningThrowingException(Set<String> testMethods, Error e) {
		Mockito.when(tester.getTestResult(TEST_CLASS.getName(), testMethods, JUnitTester.TEST_TIMEOUT)).thenThrow(e);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void mockClassLoading(Class clazz) throws Exception {
		Mockito.when(loader.loadClass(Matchers.anyString())).thenReturn(clazz);
	}

	private void mockClassLoadingThrowingException(Exception e) throws Exception {
		Mockito.when(loader.loadClass(Matchers.anyString())).thenThrow(e);
	}

	private Set<String> prepareTestMethods() {
		Set<String> testMethods = new HashSet<>();
		testMethods.add("shouldReturnHelloWorld");
		return testMethods;
	}
}
