package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.Date;
import java.util.List;

/**
 * Result of mutation analysis.
 *
 * @author pmiwaszko
 */
public interface IMutationResult {
	/**
	 * @return date of mutation analysis
	 */
	Date getDate();

	/**
	 * @return duration of mutation analysis.
	 */
	long getDuration();

	/**
	 * Get descriptions of mutation operators that where used for this mutation
	 * analysis.
	 *
	 * @return list of descriptions of mutation operators
	 */
	List<IDescriptable> getOperators();

	/**
	 * Get test classes that where used to check mutated classes against.
	 *
	 * @return list of test results
	 */
	List<ITestResult> getTests();

	/**
	 * Get results of mutation analysis for single classes.
	 *
	 * @return mutation analysis results for single class
	 */
	List<IClassMutationResult> getResults();

	/**
	 * Gets all mutants generated for given client
	 *
	 * @return mutants
	 */
	List<IMutant> getMutants();

	/**
	 * Gets all mutants count
	 *
	 * @return mutants count
	 */
	int getMutantsCount();

	/**
	 * Gets killed mutants count
	 *
	 * @return mutants count
	 */
	int getKilledMutantsCount();

	/**
	 * Gets killed by infinite loop guard count
	 *
	 * @return mutants count
	 */
	int getKilledByInfiniteLoopGuardMutantsCount();

	/**
	 * Gets killed by timeout exception count
	 *
	 * @return mutants count
	 */
	int getKilledByWorkTimeoutMutantsCount();

	/**
	 * Get number of test classes that where used to check mutated classes
	 * against.
	 *
	 * @return test count
	 */
	int getTestsCount();

	/**
	 * Get number of source classes (target classes) which were mutated
	 *
	 * @return source classes count
	 */
	int getClassesCount();

	/**
	 * Gets sum of time needed for mutant generation in multiple threads
	 *
	 * @return mutant generation duration in milliseconds
	 */
	long getMutantGenerationDuration();

	/**
	 * Gets sum of time needed for mutant evaluation (JUnit tests run duration)
	 * in multiple threads
	 *
	 * @return mutant tests duration in milliseconds
	 */
	long getTestsDuration();

}
