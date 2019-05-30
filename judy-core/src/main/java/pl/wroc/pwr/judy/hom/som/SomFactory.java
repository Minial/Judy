package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;

import java.util.List;

/**
 * Interface for classes representing Second Order Mutants (SOM) generation
 * strategy
 *
 * @author TM
 */
public interface SomFactory {

	/**
	 * Generates list of second order mutants (created from pairs of first order
	 * mutants), according to specified generation algorithm, ie.: Last to first
	 *
	 * @param foms      first order mutants
	 * @param operators list of operators used for foms creation
	 * @return list of second order mutants
	 */
	List<IMutant> create(List<IMutant> foms, List<IMutationOperator> operators);
}
