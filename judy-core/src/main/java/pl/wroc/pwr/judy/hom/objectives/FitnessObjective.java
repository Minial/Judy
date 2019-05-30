package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * HOM fitness objective, calculating fitness using different definitions of
 * fragility metric
 *
 * @author TM
 */
public class FitnessObjective implements ObjectiveCalculator {
	private String description;
	private FragilityCalculator fragilityCalculator;

	public FitnessObjective(String description, FragilityCalculator fragilityCalculator) {
		this.description = description;
		this.fragilityCalculator = fragilityCalculator;
	}

	/**
	 * Counts fitness of high order mutant
	 *
	 * @param hom  high order mutant
	 * @param foms first order mutants used to create hom
	 * @return hom fitness value
	 */
	@Override
	public double calculate(IMutant hom, List<IMutant> foms, int order) {

		/*double fomsFragility = fragilityCalculator.getFragility(foms);
		return fomsFragility == 0 ? 0 : fragilityCalculator.getFragility(hom) / fomsFragility;*/
		/* QV_Nguyen Edited
		* Calculate the ration of #TCs that kill HOM and #TCs that kill all their constituent FOMs*/

		Set<String> th = getKillingTests(hom);
		Set<String> tfs = getKillingTests(foms);
		double fitness = tfs.size();
		return fitness == 0 ? 0 : th.size() / fitness;
	}
	/*QV_Nguyen*/

	private static Set<String> getKillingTests(IMutant mutant) {
		return mutant.getResults().getFailingTestMethods();
	}

	private static Set<String> getKillingTests(List<IMutant> foms) {
		Set<String> tests = new LinkedHashSet<>();
		for (IMutant fom : foms) {
			tests.addAll(getKillingTests(fom));
		}
		return tests;
	} /*QV_Nguyen*/

	@Override
	public double getWorstValue() {
		return WORST_POSITIVE_VALUE;
	}

	@Override
	public boolean isValuable(double value) {
		return value > 0 && value < 1;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (description == null ? 0 : description.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FitnessObjective)) {
			return false;
		}
		FitnessObjective other = (FitnessObjective) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		return true;
	}
}
