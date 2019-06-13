package pl.wroc.pwr.judy.general;

import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.loader.IMutationClassLoaderFactory;
import pl.wroc.pwr.judy.loader.MutationClassLoader;
import pl.wroc.pwr.judy.loader.MutationClassLoaderFactory;
import pl.wroc.pwr.judy.research.fragility.IResearchDataCollector;
import pl.wroc.pwr.judy.research.fragility.ResearchDataCollector;
import pl.wroc.pwr.judy.tester.ITester;
import pl.wroc.pwr.judy.tester.JUnitTester;
import pl.wroc.pwr.judy.utils.Timer;
import pl.wroc.pwr.judy.work.TestDuration;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MutantEvaluator implements IMutantEvaluator {
	private ITester tester;
	private ITargetClass targetClass;
	private IBytecodeCache cache;
	private IDurationStatistic statistic;
	private long maxInfiniteLoopGuardTimeout;

	private ArrayList<IMutant> aliveMutants = new ArrayList<>();
	private ArrayList<IMutant> killedMutants = new ArrayList<>();
	private final IResearchDataCollector research = new ResearchDataCollector();

	/**
	 * Creates mutant evaluator for checking whether tests kill mutant or not
	 *
	 * @param targetClass mutated source class
	 * @param cache       bytecode cache
	 * @param tester      JUnit tester
	 * @param statistic   execution time statistics
	 */
	public MutantEvaluator(ITargetClass targetClass, IBytecodeCache cache, ITester tester,
						   IDurationStatistic statistic, long infiniteLoopGuardTimeout) {
		this.targetClass = targetClass;
		this.cache = cache;
		this.tester = tester;
		this.statistic = statistic;
		maxInfiniteLoopGuardTimeout = infiniteLoopGuardTimeout;
	}

	@Override
	public void evaluate(List<IMutant> mutants, boolean include, MatrixExecution MatrixE) {
		//System.out.println( "bug :)                  yeah !");
		List<TestDuration> tests = targetClass.getSortedTests();
		for (IMutant mutant : mutants) {
			if (!aliveMutants.contains(mutant) && !killedMutants.contains(mutant)) {
				boolean killed = areTestsKillingMutant(getCoveringTestClasses(tests, mutant), mutant, MatrixE);
				if (include) {
					saveMutant(mutant, killed);
				}
			} else {
				saveWithOldTestResults(mutant);
			}
		}
	}
	
	@Override
	public void evaluate(List<IMutant> mutants, boolean include) {
		System.out.println( "bug ?");
		List<TestDuration> tests = targetClass.getSortedTests();
		for (IMutant mutant : mutants) {
			if (!aliveMutants.contains(mutant) && !killedMutants.contains(mutant)) {
				boolean killed = areTestsKillingMutant(getCoveringTestClasses(tests, mutant), mutant);
				if (include) {
					saveMutant(mutant, killed);
				}
			} else {
				saveWithOldTestResults(mutant);
			}
		}
	}

	private List<TestDuration> getCoveringTestClasses(List<TestDuration> tests, IMutant mutant) {
		List<TestDuration> coveringTestClasses = new ArrayList<>();
		for (TestDuration testDuration : tests) {
			if (!targetClass.getCoveringMethods(testDuration.getTestClassName(), mutant.getLinesNumbers()).isEmpty()) {
				coveringTestClasses.add(testDuration);
			}
		}
		return coveringTestClasses;
	}

	/**
	 * Saves mutant with test results of equivalent mutant which has already
	 * been evaluated
	 *
	 * @param mutant
	 */
	private void saveWithOldTestResults(IMutant mutant) {
		for (ITestResult tr : getOldResults(mutant)) {
			mutant.saveResult(tr);
		}
	}

	private List<ITestResult> getOldResults(IMutant mutant) {
		int inAlive = aliveMutants.indexOf(mutant);
		return inAlive != -1 ? aliveMutants.get(inAlive).getResults() : killedMutants
				.get(killedMutants.indexOf(mutant)).getResults();
	}

	private void saveMutant(IMutant mutant, boolean killed) {
		if (killed) {
			killedMutants.add(mutant);
		} else {
			aliveMutants.add(mutant);
		}
	}

	private boolean areTestsKillingMutant(List<TestDuration> tests, IMutant mutant) {
		System.out.println( "wasp ?");
		boolean wasKilled = false;

		// mutant evaluation begins - set new classloader.
		IMutationClassLoaderFactory factory = createClassLoaderFactory(mutant.getBytecode().getBytecode(),
				getInfiniteLoopGuardTimeout(tests));
		ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
		MutationClassLoader mutationLoader = factory.createLoader();
		Thread.currentThread().setContextClassLoader(mutationLoader);

		for (TestDuration testDuration : tests) {
			Set<String> coveringTestMethods = targetClass.getCoveringMethods(testDuration.getTestClassName(),
					mutant.getLinesNumbers());
			wasKilled = isTestClassKillingMutant(testDuration, coveringTestMethods, mutant) || wasKilled;
		}
		mutant.getResults().trimToSize();

		// mutant evaluation ends - set back old classloader.
		Thread.currentThread().setContextClassLoader(contextLoader);
		return wasKilled;
	}
	
	private boolean areTestsKillingMutant(List<TestDuration> tests, IMutant mutant, MatrixExecution MatrixE) {
		boolean wasKilled = false;

		// mutant evaluation begins - set new classloader.
		IMutationClassLoaderFactory factory = createClassLoaderFactory(mutant.getBytecode().getBytecode(),
				getInfiniteLoopGuardTimeout(tests));
		ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
		MutationClassLoader mutationLoader = factory.createLoader();
		Thread.currentThread().setContextClassLoader(mutationLoader);

		for (TestDuration testDuration : tests) {
			//System.out.println( "waaaaaaaaaaaaaaaaaaaaaaaaasp !");
			Set<String> coveringTestMethods = targetClass.getCoveringMethods(testDuration.getTestClassName(),
					mutant.getLinesNumbers());
			wasKilled = isTestClassKillingMutant(testDuration, coveringTestMethods, mutant, MatrixE) || wasKilled;
		}
		mutant.getResults().trimToSize();

		// mutant evaluation ends - set back old classloader.
		Thread.currentThread().setContextClassLoader(contextLoader);
		return wasKilled;
	}

	public long getInfiniteLoopGuardTimeout(List<TestDuration> tests) {
		long longestDuration = 0;
		for (TestDuration testDuration : tests) {
			if (testDuration.getDuration() > longestDuration) {
				longestDuration = testDuration.getDuration();
			}
		}
		long infiniteLoopGuardTimeout = longestDuration;
		return infiniteLoopGuardTimeout < maxInfiniteLoopGuardTimeout ? infiniteLoopGuardTimeout
				: maxInfiniteLoopGuardTimeout;
	}

	public long getTestClassExecutionTimeout(long testClassDuration) {
		long timeout = JUnitTester.TEST_TIMEOUT;
		if (testClassDuration > 0 && testClassDuration * 4 < JUnitTester.TEST_TIMEOUT) {
			timeout = 4 * testClassDuration;
		}
		return timeout;
	}

	private boolean isTestClassKillingMutant(TestDuration test, Set<String> coveringTestMethods, IMutant mutant) {
		Timer timer = new Timer();
		// In case something really time consuming happen and it is not handled
		// by infinite loop guard:
		long testClassExecutionTimeout = getTestClassExecutionTimeout(test.getDuration());

		ITestResult testrunResult = tester.getTestResult(test.getTestClassName(), coveringTestMethods,
				testClassExecutionTimeout);

		mutant.saveResult(testrunResult);

		statistic.addTestRun(timer.getDuration());
		statistic.addTestMethods(testrunResult.getTestMethods().size());
		saveResearchData(mutant, testrunResult);

		//System.out.println("mutant id " + mutant.getId() + "\t test name : " + test.getTestClassName() + "\t test passed : " + testrunResult.passed());
		//System.out.println( "waaaaaaaaaaaaaaaaaaaaaaaaasp ?");
		//MatrixE_.test();
		//System.out.println( "waaaaaaaaaaaaaaaaaaaaaaaaasp !!");

		return !testrunResult.passed();
	}
	
	private boolean isTestClassKillingMutant(TestDuration test, Set<String> coveringTestMethods, IMutant mutant, MatrixExecution MatrixE) {
		//System.out.println( "waaaaaaaaaaaaaaaaaaaaaaaaasp ?");
		Timer timer = new Timer();
		// In case something really time consuming happen and it is not handled
		// by infinite loop guard:
		long testClassExecutionTimeout = getTestClassExecutionTimeout(test.getDuration());

		ITestResult testrunResult = tester.getTestResult(test.getTestClassName(), coveringTestMethods,
				testClassExecutionTimeout);

		mutant.saveResult(testrunResult);

		statistic.addTestRun(timer.getDuration());
		statistic.addTestMethods(testrunResult.getTestMethods().size());
		saveResearchData(mutant, testrunResult);
		
		String temp = "" + mutant.getId();
		//System.out.println("mutant id " + temp + "\t test name : " + test.getTestClassName() + "\t test passed : " + testrunResult.passed());
		MatrixE.addResult(temp, test.getTestClassName(), testrunResult.passed());
		//MatrixE_.test();

		return !testrunResult.passed();
	}

	protected void saveResearchData(IMutant mutant, ITestResult testrunResult) {
		research.saveResult(targetClass, mutant, testrunResult);
	}

	protected IMutationClassLoaderFactory createClassLoaderFactory(byte[] mutantBytecode, long timeout) {
		return new MutationClassLoaderFactory(targetClass.getName(), mutantBytecode, cache, timeout);
	}

	/**
	 * @return the aliveMutants
	 */
	@Override
	public List<IMutant> getAliveMutants() {
		return aliveMutants;
	}

	/**
	 * @return the killedMutants
	 */
	@Override
	public List<IMutant> getKilledMutants() {
		return killedMutants;
	}

	@Override
	public void trimSize() {
		aliveMutants.trimToSize();
		killedMutants.trimToSize();
	}
}
