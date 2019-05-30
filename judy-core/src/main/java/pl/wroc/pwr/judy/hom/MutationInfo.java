package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IMutationOperator;

/**
 * Data structure for mutation operator use at single mutation point
 *
 * @author TM
 */
public class MutationInfo {
	private IMutationOperator operator;
	private int mutationPointIndex;
	private int mutantIndex;

	/**
	 * Creates mutation info
	 *
	 * @param operator           mutation operator
	 * @param mutationPointIndex mutation point index
	 * @param mutantIndex        within given point
	 */
	public MutationInfo(IMutationOperator operator, int mutationPointIndex, int mutantIndex) {
		this.operator = operator;
		this.mutationPointIndex = mutationPointIndex;
		this.mutantIndex = mutantIndex;
	}

	/**
	 * Gets mutation point
	 *
	 * @return mutation point
	 */
	public int getMutationPointIndex() {
		return mutationPointIndex;
	}

	/**
	 * Gets mutant index within given point
	 *
	 * @return mutant index
	 */
	public int getMutantIndex() {
		return mutantIndex;
	}

	/**
	 * Gets mutation operator
	 *
	 * @return the operator
	 */
	public IMutationOperator getOperator() {
		return operator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mutantIndex;
		result = prime * result + mutationPointIndex;
		result = prime * result + (operator == null ? 0 : operator.hashCode());
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
		if (!(obj instanceof MutationInfo)) {
			return false;
		}
		MutationInfo other = (MutationInfo) obj;
		if (mutantIndex != other.mutantIndex) {
			return false;
		}
		if (mutationPointIndex != other.mutationPointIndex) {
			return false;
		}
		if (operator == null) {
			if (other.operator != null) {
				return false;
			}
		} else if (!operator.equals(other.operator)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getOperator().getName() + "@" + mutationPointIndex + "_" + mutantIndex;
	}

}
