package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;

public class UniqueLinesHomFactory extends AbstractHomFactory {

	/**
	 * Creates HOM factory not allowing multiple mutations in the same line
	 *
	 * @param statistic duration statistics
	 */
	public UniqueLinesHomFactory(IDurationStatistic statistic) {
		super(statistic);
	}

	@Override
	protected boolean isMutationValid(IMutant hom, IMutantBytecode mutationBytecode) {
		return !hom.getLinesNumbers().contains(mutationBytecode.getLineNumber());
	}

}
