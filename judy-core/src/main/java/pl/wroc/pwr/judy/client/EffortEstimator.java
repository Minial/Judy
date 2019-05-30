package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.general.ICoverage;

import java.util.List;

public class EffortEstimator {

	private static final float HUNDRED_PERCENT = 100;
	private List<String> targetClasses;
	private ICoverage coverage;
	private float tests;
	private float factor;

	/**
	 * Creates mutation process effort estimator
	 *
	 * @param tests         number of passing tests
	 * @param targetClasses source classes to be mutated
	 * @param coverage      test coverage data
	 */
	public EffortEstimator(int tests, List<String> targetClasses, ICoverage coverage) {
		this.tests = tests;
		this.coverage = coverage;
		this.targetClasses = targetClasses;
		factor = countFactor();
	}

	/**
	 * Estimates effort for specified target class
	 *
	 * @param targetClass source class name
	 * @return estimated effort
	 */
	public float estimate(String targetClass) {
		return factor * coverage.countCoveringTestClasses(targetClass) / tests;
	}

	/**
	 * Gets factor used for effort estimation
	 *
	 * @return effort factor
	 */
	public float getEffortFactor() {
		return factor;
	}

	private float countFactor() {
		float totalEffort = 0.0f;
		for (String targetClass : targetClasses) {
			totalEffort += coverage.countCoveringTestClasses(targetClass);
		}
		return HUNDRED_PERCENT * tests / totalEffort;
	}
}
