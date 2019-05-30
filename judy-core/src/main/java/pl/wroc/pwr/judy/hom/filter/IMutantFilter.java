package pl.wroc.pwr.judy.hom.filter;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

public interface IMutantFilter {
	/**
	 * Filters out mutant which are not fulfilling specific criteria
	 *
	 * @param mutants collection of mutants to be filtered
	 * @return filtered collection of mutants
	 */
	List<IMutant> filter(List<IMutant> mutants);
}
