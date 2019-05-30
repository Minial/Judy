package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;

public class BaseHomFactory extends AbstractHomFactory {

	/**
	 * Creates HOM factory
	 *
	 * @param statistic duration statistics
	 */
	public BaseHomFactory(IDurationStatistic statistic) {
		super(statistic);
	}

	@Override
	protected boolean isMutationValid(IMutant hom, IMutantBytecode mutationBytecode) {
		return true;
	}

}
