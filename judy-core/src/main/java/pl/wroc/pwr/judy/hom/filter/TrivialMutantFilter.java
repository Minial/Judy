package pl.wroc.pwr.judy.hom.filter;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.ArrayList;
import java.util.List;

public class TrivialMutantFilter implements IMutantFilter {

	@Override
	public List<IMutant> filter(List<IMutant> mutants) {
		ArrayList<IMutant> filteredMutants = new ArrayList<>();

		for (IMutant mutant : mutants) {
			if (!isTrivial(mutant)) {
				filteredMutants.add(mutant);
			}
		}
		filteredMutants.trimToSize();
		return filteredMutants;
	}

	public boolean isTrivial(IMutant mutant) {
		int all = 0;
		int failed = 0;
		failed += mutant.getResults().getFailingTestMethods().size();
		all += mutant.getResults().getTestMethods().size();
		return failed == all;
	}

}
