package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

/**
 * LastToFirst strategy for generating SOMs. Combining first order mutants in a
 * specific order: last mutant with the first in the list, one before last with
 * the second, and so on. Every first order mutant is used once, except the
 * situation when list of mutants is odd. Then one of the first order mutants is
 * used twice.
 */
public class LastToFirstStrategy extends AbstractOrderStrategy {

	@Override
	protected int getNextFomIndex(List<IMutant> foms) {
		return foms.size() - 1;
	}
}
