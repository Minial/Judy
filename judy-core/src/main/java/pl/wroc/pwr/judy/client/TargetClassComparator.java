package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.IInitialTestsRun;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.util.Comparator;

/**
 * Comparator represent heuristic for target class mutation order based on class
 * size and tests duration. Bigger classes or classes with higher test duration
 * are considered higher then the opposite.
 *
 * @author TM
 */
public final class TargetClassComparator implements Comparator<String> {
	private final IBytecodeCache cache;
	private final IInitialTestsRun tests;

	/**
	 * Default constructor, comparator need bytecode cache for identifying class
	 * size and initial tests run for test duration data.
	 *
	 * @param cache bytecode cache of target classes
	 * @param tests initial test run
	 */
	public TargetClassComparator(IBytecodeCache cache, IInitialTestsRun tests) {
		this.cache = cache;
		this.tests = tests;
	}

	@Override
	public int compare(String class1, String class2) {
		long v1 = getSize(class1) * getDuration(class1);
		long v2 = getSize(class2) * getDuration(class2);
		return Long.valueOf(v2).compareTo(v1);
	}

	/**
	 * Gets class size (bytecode size)
	 *
	 * @param className target class name
	 * @return length of bytecode array
	 */
	private int getSize(String className) {
		return cache.get(className).length;
	}

	/**
	 * Gets sum of passing tests duration
	 *
	 * @param className target class name
	 * @return sum of tests duration
	 */
	private long getDuration(String className) {
		long sum = 0;
		for (String tc : tests.getCoverage().getCoveringTestClasses(className)) {
			for (ITestResult result : tests.getPassingResults()) {
				if (tc.equals(result.getClassName())) {
					sum += result.getDuration();
				}
			}
		}
		return sum;
	}
}
