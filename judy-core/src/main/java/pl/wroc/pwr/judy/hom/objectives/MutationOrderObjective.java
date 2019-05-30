package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

/**
 * HOM mutation order objective, calculating number of mutations introduced in
 * bytecode
 *
 * @author TM
 */
public class MutationOrderObjective implements ObjectiveCalculator {

	private String description;

	/**
	 * Creates mutation order objective
	 *
	 * @param description description
	 */
	public MutationOrderObjective(String description) {
		this.description = description;
	}

	@Override
	public double calculate(IMutant hom, List<IMutant> usedFoms, int order) {
		return order > 1 ? order : getWorstValue(); // we want HOMs, not FOMs
	}

	@Override
	public double getWorstValue() {
		return WORST_POSITIVE_VALUE;
	}

	@Override
	public String getDescription() {
		return description;
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
		if (!(obj instanceof MutationOrderObjective)) {
			return false;
		}
		MutationOrderObjective other = (MutationOrderObjective) obj;
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
