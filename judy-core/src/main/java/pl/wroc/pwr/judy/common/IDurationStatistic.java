package pl.wroc.pwr.judy.common;

import java.io.Serializable;

/**
 * Duration statistics of mutation work.
 *
 * @author pmiwaszko
 */
public interface IDurationStatistic extends Serializable {
	/**
	 * Get sum of durations of executed tests.
	 *
	 * @return duration in milliseconds
	 */
	long getTestsDuration();

	/**
	 * Get sum of durations of generated mutants.
	 *
	 * @return duration in milliseconds
	 */
	long getGenerationDuration();

	/**
	 * Adds tests execution time to overall testing time
	 *
	 * @param duration execution time in milliseconds
	 */
	void addTestRun(long duration);

	/**
	 * Adds single mutant generation time to overall mutant generation time.
	 *
	 * @param duration mutant generation time in milliseconds
	 */
	void addMutantGenration(long duration);

	/**
	 * Adds single initial test run execution time
	 *
	 * @param duration initial test run duration in milliseconds
	 */
	void addInitialTest(long duration);

	/**
	 * Get overall duration of initial tests execution.
	 *
	 * @return initial tests execution duration in milliseconds
	 */
	long getInitialTestsDuration();

	/**
	 * Adds number of executed test methods
	 *
	 * @param methodsCount number of executed test methods
	 */
	void addTestMethods(int methodsCount);

	/**
	 * Gets sum of all executed test methods
	 *
	 * @return number of executed test methods
	 */
	int getTestMethods();
}
