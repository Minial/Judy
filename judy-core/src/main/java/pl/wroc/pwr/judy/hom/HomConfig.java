package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.hom.objectives.IObjectivesFactory;

public class HomConfig {
	private int maxEvaluations;
	private boolean skipSameLineMutations;
	private boolean skipTrivialMutantFilter;
	private IObjectivesFactory objectivesFactory;
	private HomStrategy homStrategy;
	private int maxMutationOrder;

	/**
	 * Gets maximum HOM evaluations
	 *
	 * @return the maxEvaluations
	 */
	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	/**
	 * Sets maximum HOM evaluations
	 *
	 * @param maxEvaluations the maxEvaluations to set
	 */
	public void setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;
	}

	/**
	 * @return true if same line mutations should be skipped during mutation
	 * process
	 */
	public boolean isSkippingSameLineMutations() {
		return skipSameLineMutations;
	}

	/**
	 * @return true if trivial first order mutants should be included in HOM
	 * generation
	 */
	public boolean shouldSkipTrivialMutantFilter() {
		return skipTrivialMutantFilter;
	}

	/**
	 * Sets skipping of trivial mutants during HOM generation
	 *
	 * @param skipTrivialMutantFilter the skipTrivialMutantFilter to set
	 */
	public void setSkipTrivialMutantFilter(boolean skipTrivialMutantFilter) {
		this.skipTrivialMutantFilter = skipTrivialMutantFilter;
	}

	/**
	 * Sets skipping of same line mutations
	 *
	 * @param skipSameLineMutations the sameLineMutations to set
	 */
	public void setSkippingSameLineMutations(boolean skipSameLineMutations) {
		this.skipSameLineMutations = skipSameLineMutations;
	}

	/**
	 * Gets HOM mutation objectives factory
	 *
	 * @return the objectivesFactory
	 */
	public IObjectivesFactory getObjectivesFactory() {
		return objectivesFactory;
	}

	/**
	 * Sets HOM mutation objectives factory
	 *
	 * @param objectivesFactory the objectivesFactory to set
	 */
	public void setObjectivesFactory(IObjectivesFactory objectivesFactory) {
		this.objectivesFactory = objectivesFactory;
	}

	/**
	 * Gets HOM mutation strategy
	 *
	 * @return the homStrategy
	 */
	public HomStrategy getStrategy() {
		return homStrategy;
	}

	/**
	 * Sets HOM mutation strategy
	 *
	 * @param homStrategy the homStrategy to set
	 */
	public void setStrategy(HomStrategy homStrategy) {
		this.homStrategy = homStrategy;
	}

	/**
	 * Gets maximum mutation order limit for generated HOMs
	 *
	 * @return maximum mutation order
	 */
	public int getMaxMutationOrder() {
		return maxMutationOrder;
	}

	/**
	 * Sets maximum mutation order limit for generated HOMs
	 *
	 * @param maxMutationOrder maximum mutation order to set
	 */
	public void setMaxMutationOrder(int maxMutationOrder) {
		this.maxMutationOrder = maxMutationOrder;
	}
}
