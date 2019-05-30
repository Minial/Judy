package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IMutant;

public interface IMutantCache {

	/**
	 * Gets mutants cached for mutation operator used at specified point
	 *
	 * @param mutationInfo mutation info (operator, point, index)
	 * @return cached mutant
	 */
	IMutant get(MutationInfo mutationInfo);

	/**
	 * Adds mutants created at specified point
	 *
	 * @param mutationInfo mutation info (operator, point, index)
	 * @param mutant       mutant
	 */
	void add(MutationInfo mutationInfo, IMutant mutant);

}
