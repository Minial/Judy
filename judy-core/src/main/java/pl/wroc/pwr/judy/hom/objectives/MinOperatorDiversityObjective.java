package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

/**
 * Minimizes HOM operators diversity. Operators diversity is ratio of unique
 * operators and all operators used to create high order mutant. Value 1 ->
 * every operator is used for one mutation only, value near 0 -> every operator
 * is used for multiple mutations.
 *
 * @author TM
 */
public class MinOperatorDiversityObjective extends MaxOperatorDiversityObjective {

	/**
	 * Creates operator diversity objective
	 *
	 * @param description description
	 */
	public MinOperatorDiversityObjective(String description) {
		super(description);
	}

	@Override
	public double calculate(IMutant hom, List<IMutant> foms, int order) {
		return -super.calculate(hom, foms, order);
	}
}
