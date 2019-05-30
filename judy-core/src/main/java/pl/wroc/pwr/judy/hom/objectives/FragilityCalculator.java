package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Fragility calculator based on Jia's proposal for calculation of fragility
 * using fitness(foms) and fitness(hom). Uses failing test methods union for
 * counting fitness(foms). Duplicated test methods in scope of a single mutant
 * are ignored.
 * <p/>
 * Given FOM_A and its test methods {x,x,y,z}, and FOM_B and its test methods
 * {x,z,z}. Fragility for FOM_A uses |{x,y,z}|=3 as number of tests, for FOM_B
 * it is |{x,z}|=2. Counting fragility({FOM_A, FOM_B}) uses
 * |{x,y,z}+{x,z}|=|{x,y,z}|=3 as number of tests for mutants FOM_A and FOM_B.
 *
 * @author TM, Maciej Szewczyszyn
 */
public class FragilityCalculator {

	/**
	 * Counts fragility for single mutant
	 *
	 * @param mutant mutant
	 * @return fragility
	 */
	public double getFragility(IMutant mutant) {
		int testMethods = mutant.getResults().getTestMethods().size();
		return testMethods == 0 ? 0 : (double) mutant.getResults().getFailingTestMethods().size() / testMethods;
	}

	/**
	 * Count fragility for multiple first order mutants
	 *
	 * @param foms first order mutants
	 * @return fragility
	 */
	public double getFragility(List<IMutant> foms) {

		double testCases = countTestCases(foms);
		return testCases == 0 ? 0 : countKillingTestCases(foms) / testCases;

	}

	private double countKillingTestCases(List<IMutant> foms) {
		Set<String> killingTestCases = new HashSet<>();

		for (IMutant fom : foms) {
			killingTestCases.addAll(fom.getResults().getFailingTestMethods());
		}
		return killingTestCases.size();
	}

	private double countTestCases(List<IMutant> foms) {
		Set<String> testCases = new HashSet<>();
		for (IMutant fom : foms) {
			testCases.addAll(fom.getResults().getTestMethods());
		}
		return testCases.size();
	}
}
