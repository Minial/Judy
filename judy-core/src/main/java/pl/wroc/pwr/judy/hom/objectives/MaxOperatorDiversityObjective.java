package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashSet;
import java.util.List;

/**
 * Maximizes HOM operators diversity. Operators diversity is ratio of unique
 * operators and all operators used to create high order mutant. Value 1 ->
 * every operator is used for one mutation only, value near 0 -> every operator
 * is used for multiple mutations.
 *
 * @author TM
 */
public class MaxOperatorDiversityObjective implements ObjectiveCalculator {

	private String description;

	/**
	 * Creates operator diversity objective
	 *
	 * @param description description
	 */
	public MaxOperatorDiversityObjective(String description) {
		this.description = description;
	}

	@Override
	public double calculate(IMutant hom, List<IMutant> foms, int order) {
		List<String> operators = hom.getOperatorsNames();
		double diversity = 0.0;
		if (operators.size() > 0) {
			diversity = countDiversity(operators);
		}
		return -diversity;
	}

	@Override
	public double getWorstValue() {
		return WORST_POSITIVE_VALUE;
	}

	@Override
	public String getDescription() {
		return description;
	}

	private double countDiversity(List<String> operators) {
		return countUnique(operators) / operators.size();
	}

	private double countUnique(List<String> operators) {
		return new HashSet<>(operators).size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (description == null ? 0 : description.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MaxOperatorDiversityObjective)) {
			return false;
		}
		MaxOperatorDiversityObjective other = (MaxOperatorDiversityObjective) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValuable(double value) {
		return true;
	}

}
