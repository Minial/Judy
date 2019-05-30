package pl.wroc.pwr.judy.hom.filter;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

public class DummyFilter implements IMutantFilter {

	@Override
	public List<IMutant> filter(List<IMutant> mutants) {
		return mutants;
	}
}
