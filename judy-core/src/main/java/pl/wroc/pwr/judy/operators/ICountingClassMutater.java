package pl.wroc.pwr.judy.operators;

import pl.wroc.pwr.judy.common.IMutationPoint;

import java.util.List;

/**
 * Base interface for class mutaters that count possible mutations.
 *
 * @author pmiwaszko
 */
public interface ICountingClassMutater {
	/**
	 * Get list of possible mutation points.
	 */
	List<IMutationPoint> getMutationPoints();
}
