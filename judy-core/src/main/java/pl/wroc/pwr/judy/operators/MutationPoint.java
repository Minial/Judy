package pl.wroc.pwr.judy.operators;

import pl.wroc.pwr.judy.common.IMutationPoint;

/**
 * Immutable implementation of mutation point.
 *
 * @author pmiwaszko
 */
public class MutationPoint implements IMutationPoint {
	private final int index;
	private final int mutantsCount;

	/**
	 * <code>MutationPoint</code> constructor.
	 */
	public MutationPoint(int index) {
		this(index, 0);
	}

	/**
	 * <code>MutationPoint</code> constructor.
	 */
	public MutationPoint(int index, int mutantsCount) {
		this.index = index;
		this.mutantsCount = mutantsCount;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getMutantsCount() {
		return mutantsCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		if (!(obj instanceof MutationPoint)) {
			return false;
		}
		MutationPoint other = (MutationPoint) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}

}
