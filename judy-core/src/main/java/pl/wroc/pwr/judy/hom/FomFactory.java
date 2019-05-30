package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IMutant;

public interface FomFactory {

	/**
	 * Creates FOMs using mutation operator on provided target class' bytecode
	 *
	 * @param mi mutation info (operator, point, mutantIndex)
	 * @return FOM
	 */
	IMutant createFom(MutationInfo mi);

}
