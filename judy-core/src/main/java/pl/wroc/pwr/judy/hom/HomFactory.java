package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;

import java.util.List;

public interface HomFactory {

	/**
	 * Creates single high order mutant, using given first order mutants
	 *
	 * @param foms      first order mutants to generate HOM with
	 * @param operators mutation operators
	 * @return high order mutant, HOM
	 * @throws IncompatibleMutationException thrown if given set of FOMs cannot be reproduced as HOM
	 *                                       containing all mutations
	 */
	IMutant create(List<IMutant> foms, List<IMutationOperator> operators) throws IncompatibleMutationException;
}
