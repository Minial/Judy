package pl.wroc.pwr.judy.general;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.loader.IMutationClassLoaderFactory;
import pl.wroc.pwr.judy.loader.MutationClassLoader;
import pl.wroc.pwr.judy.operators.MutantBytecode;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;
import pl.wroc.pwr.judy.tester.ITester;
import pl.wroc.pwr.judy.work.Mutant;
import pl.wroc.pwr.judy.work.TestDuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class MutantEvaluatorTest {
	private ITargetClass targetClass;
	private IBytecodeCache cache;
	private ITester tester;
	private IDurationStatistic statistic;
	private IMutationClassLoaderFactory factory;
	private long timeout = InfiniteLoopGuard.DEFAULT_TIMEOUT;

	private MutantEvaluator evaluator;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		targetClass = mockTargetClassWithSortedTests();
		cache = Mockito.mock(IBytecodeCache.class);
		tester = Mockito.mock(ITester.class);
		statistic = Mockito.mock(IDurationStatistic.class);
		factory = Mockito.mock(IMutationClassLoaderFactory.class);
		Mockito.when(factory.createLoader()).thenReturn(Mockito.mock(MutationClassLoader.class));

		evaluator = createPartiallyMockedEvaluator();
	}

	private MutantEvaluator createPartiallyMockedEvaluator() {
		return new MutantEvaluator(targetClass, cache, tester, statistic, timeout) {
			@Override
			protected IMutationClassLoaderFactory createClassLoaderFactory(byte[] mutantBytecode, long timeout) {
				return factory;
			}
		};
	}

	@Test
	public void shouldNotEvaluateIfNoCoveringMethods() throws Exception {
		targetClass = mockTargetClassWithSortedTestsWithoutCoveringMethods();
		evaluator = createPartiallyMockedEvaluator();

		evaluator.evaluate(Arrays.asList(createMutant()), true);

		assertTrue(evaluator.getKilledMutants().isEmpty());
		assertEquals(1, evaluator.getAliveMutants().size());
		verifyTesterNotInvoked();
	}

	@Test
	public void shouldNotEvaluateAlreadyKilledMutantAgain() throws Exception {
		ITestResult failed = mockTestResult(false);
		mockTesterRun(failed, failed, failed);

		evaluator.evaluate(Arrays.asList(createMutant()), true);
		evaluator.evaluate(Arrays.asList(createMutant()), true);

		assertEquals(1, evaluator.getKilledMutants().size());
		verifyTester();
	}

	@Test
	public void shouldRunMultipleFailingTestsAndSaveMutantAsKilled() throws Exception {
		ITestResult failed = mockTestResult(false);
		mockTesterRun(failed, failed, failed);

		evaluator.evaluate(Arrays.asList(createMutant()), true);

		assertEquals(1, evaluator.getKilledMutants().size());
		verifyTester();
	}

	@Test
	public void shouldNotIncludeEvalutedMutants() throws Exception {
		ITestResult failed = mockTestResult(false);
		ITestResult passed = mockTestResult(true);
		mockTesterRun(failed, passed, failed);

		evaluator.evaluate(Arrays.asList(createMutant()), false);

		assertTrue(evaluator.getKilledMutants().isEmpty());
		assertTrue(evaluator.getAliveMutants().isEmpty());
		verifyTester();
	}

	@Test
	public void shouldRunMultipleTestsAndSaveMutantAsKilled() throws Exception {
		ITestResult failed = mockTestResult(false);
		ITestResult passed = mockTestResult(true);

		mockTesterRun(passed, failed, passed);

		evaluator.evaluate(Arrays.asList(createMutant()), true);

		assertEquals(1, evaluator.getKilledMutants().size());
		verifyTester();
	}

	@Test
	public void shouldNotEvaluateAlreadyKnownAliveMutantAgain() throws Exception {
		ITestResult passed = mockTestResult(true);
		ITestResult lastResult = mockTestResult(true);

		mockTesterRun(passed, passed, lastResult);
		evaluator.evaluate(Arrays.asList(createMutant()), true);
		evaluator.evaluate(Arrays.asList(createMutant()), true);

		assertEquals(1, evaluator.getAliveMutants().size());
		verifyTester();
	}

	@Test
	public void shouldRunSuccessfulTestsAndSaveMutantAsAlive() throws Exception {
		ITestResult passed = mockTestResult(true);
		ITestResult lastResult = mockTestResult(true);

		mockTesterRun(passed, passed, lastResult);
		IMutant mutant = createMutant();
		evaluator.evaluate(Arrays.asList(mutant), true);

		assertEquals(0, evaluator.getKilledMutants().size());
		assertEquals(1, evaluator.getAliveMutants().size());
		assertEquals(3, mutant.getResults().size());
		verifyTester();
	}

	@Test
	public void shouldCreateClassLoaderFactory() throws Exception {
		evaluator = new MutantEvaluator(targetClass, cache, tester, statistic, timeout);
		IMutationClassLoaderFactory result = evaluator.createClassLoaderFactory(new byte[]{1, 2, 3}, 0);
		assertNotNull(result);
	}

	private void verifyTester() {
		Set<String> coveringMethods = new HashSet<>();
		coveringMethods.add("testMethod");
		Mockito.verify(tester).getTestResult("a", coveringMethods, 400);
		Mockito.verify(tester).getTestResult("b", coveringMethods, 800);
		Mockito.verify(tester).getTestResult("c", coveringMethods, 1200);
	}

	private void verifyTesterNotInvoked() {
		Mockito.verify(tester, Mockito.never()).getTestResult("a", new HashSet<String>(), 400);
		Mockito.verify(tester, Mockito.never()).getTestResult("b", new HashSet<String>(), 800);
		Mockito.verify(tester, Mockito.never()).getTestResult("c", new HashSet<String>(), 1200);
	}

	@SuppressWarnings("unchecked")
	private void mockTesterRun(ITestResult resultA, ITestResult resultB, ITestResult resultC) {
		Mockito.when(tester.getTestResult(Matchers.anyString(), Matchers.anySet(), Matchers.anyInt())).thenReturn(
				resultA, resultB, resultC);
	}

	private IMutant createMutant() {
		Mutant mutant = new Mutant("", 0, 0, "", 0, 0);
		mutant.setBytecode(new MutantBytecode(new byte[]{1, 2, 3}, 0));
		return mutant;
	}

	private ITargetClass mockTargetClassWithSortedTests() {
		ITargetClass targetClass = Mockito.mock(ITargetClass.class);
		List<TestDuration> td = Arrays.asList(new TestDuration("a", 100), new TestDuration("b", 200), new TestDuration(
				"c", 300));
		Mockito.when(targetClass.getSortedTests()).thenReturn(td);
		Set<String> coveringMethods = new HashSet<>();
		coveringMethods.add("testMethod");
		Mockito.when(targetClass.getCoveringMethods(Matchers.anyString(), Matchers.anyListOf(Integer.class)))
				.thenReturn(coveringMethods);
		return targetClass;
	}

	private ITargetClass mockTargetClassWithSortedTestsWithoutCoveringMethods() {
		ITargetClass targetClass = Mockito.mock(ITargetClass.class);
		List<TestDuration> td = Arrays.asList(new TestDuration("a", 100), new TestDuration("b", 200), new TestDuration(
				"c", 300));
		Mockito.when(targetClass.getSortedTests()).thenReturn(td);
		Mockito.when(targetClass.getCoveringMethods(Matchers.anyString(), Matchers.anyListOf(Integer.class)))
				.thenReturn(new HashSet<String>());
		return targetClass;
	}

	private ITestResult mockTestResult(boolean passed) {
		ITestResult testResult = Mockito.mock(ITestResult.class);
		Mockito.when(testResult.passed()).thenReturn(passed);
		return testResult;
	}

	@Test
	public void shouldCalculateTestClassExecutionTimeoutUsingDuration() throws Exception {
		checkTimeout(300, 1200);
	}

	private void checkTimeout(long duration, long expectedTimeout) {
		assertEquals(expectedTimeout, evaluator.getTestClassExecutionTimeout(duration));
	}
}
