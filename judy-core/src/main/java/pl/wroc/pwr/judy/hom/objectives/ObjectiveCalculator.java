/**
 *
 */
package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

/**
 * Interface for all best HOM problem objective value calculators.
 *
 * @author TM
 */
public interface ObjectiveCalculator {
	int WORST_POSITIVE_VALUE = Integer.MAX_VALUE;

	/**
	 * Calculates objective value
	 *
	 * @param hom  high order mutant to evaluate
	 * @param foms first order mutants used for hom creation
	 * @return objective value which may be minimized (i.e. accumulative metric
	 * should be negative)
	 */
	double calculate(IMutant hom, List<IMutant> foms, int order);


	/**
	 * Gets invalid, impossible value for given metric.
	 *
	 * @return invalid value
	 */
	double getWorstValue();

	/**
	 * Verifies if given value of objective indicates a valuable mutant of high
	 * quality
	 *
	 * @param value calculated objective value
	 * @return true if objective value is high quality
	 */
	boolean isValuable(double value);

	/**
	 * Gets calculator's description
	 *
	 * @return description
	 */
	String getDescription();
}
