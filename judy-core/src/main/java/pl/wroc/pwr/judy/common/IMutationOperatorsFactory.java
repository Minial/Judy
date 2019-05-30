package pl.wroc.pwr.judy.common;

import java.io.Serializable;
import java.util.List;

/**
 * Interface of mutation operators factory.
 *
 * @author pmiwaszko
 */
public interface IMutationOperatorsFactory extends Serializable {
	/**
	 * Get list of mutation operators instances.
	 */
	List<IMutationOperator> create(IEnvironment env);

	/**
	 * Get list of descriptions of mutation operators.
	 */
	List<IDescriptable> getDescriptions();
}
