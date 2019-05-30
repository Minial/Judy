package pl.wroc.pwr.judy.hom.research;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class HomTypeVerifier {
	private HomTypeVerifier() {
	}

	/**
	 * Verifies if HOM mutant is subsuming
	 *
	 * @param hom  HOM mutant
	 * @param foms FOM mutants used for HOM generation
	 * @return true if set of tests killing HOM is not empty and its size is
	 * lower than size of tests killing FOMs
	 */
	public static boolean isSubsuming(IMutant hom, List<IMutant> foms) {
		Set<String> th = getKillingTests(hom);
		Set<String> tis = getKillingTests(foms);
		return !th.isEmpty() && th.size() < tis.size();
	}

	/**
	 * Verifies if sets of tests killing FOMs are intersecting
	 *
	 * @param hom  HOM mutant
	 * @param foms FOM mutants used for HOM generation
	 * @return true if sets do not intersect
	 */
	public static boolean isNonIntersecting(IMutant hom, List<IMutant> foms) {
		return /*!getKillingTests(hom).isEmpty() &&*/ getTestsIntersection(foms).isEmpty();
	}

	/**
	 * Verifies if hom is of Partial Fault Shifting type (described in Yue Jia
	 * PhD)
	 *
	 * @param hom  HOM
	 * @param foms FOMs used for HOM generation
	 * @return true if hom is of Partial Fault Shifting type
	 */
	public static boolean isPartiallyShifted(IMutant hom, List<IMutant> foms) {
		Set<String> testsKillingHom = getKillingTests(hom);
		Set<String> testsKillingFoms = getKillingTests(foms);
		return !testsKillingHom.isEmpty() && !testsKillingFoms.containsAll(testsKillingHom);
	}

	/**
	 * Verifies if hom is of Total Fault Shifting type (described in Yue Jia
	 * PhD)
	 *
	 * @param hom  HOM
	 * @param foms FOMs used for HOM generation
	 * @return true if hom is of Total Fault Shifting type
	 */
	public static boolean isTotallyShifted(IMutant hom, List<IMutant> foms) {
		Set<String> testsKillingHom = getKillingTests(hom);
		Set<String> testsKillingFoms = getKillingTests(foms);
		if (!testsKillingHom.isEmpty()) {
			for (String test : testsKillingFoms) {
				if (testsKillingHom.contains(test)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Verifies if HOM mutant is strongly subsuming
	 *
	 * @param hom  HOM mutant
	 * @param foms FOM mutants used for HOM generation
	 * @return true if set of tests killing HOM is not empty and it is contained
	 * in intersection of tests killing FOMs
	 */
	public static boolean isStronglySubsuming(IMutant hom, List<IMutant> foms) {
		Set<String> th = getKillingTests(hom);
		Set<String> intersection = getTestsIntersection(foms);
		boolean containsAll = intersection.containsAll(th);
		return !intersection.isEmpty() && containsAll && !th.isEmpty();
	}

	private static Set<String> getTestsIntersection(List<IMutant> foms) {
		Set<String> intersection = null;

		for (IMutant fom : foms) {
			if (intersection == null) {
				intersection = new HashSet<>();
				intersection.addAll(getKillingTests(fom));
				continue;
			}
			intersection.retainAll(getKillingTests(fom));
		}
		return intersection;
	}

	private static Set<String> getKillingTests(IMutant mutant) {
		return mutant.getResults().getFailingTestMethods();
	}

	private static Set<String> getKillingTests(List<IMutant> foms) {
		Set<String> tests = new LinkedHashSet<>();
		for (IMutant fom : foms) {
			tests.addAll(getKillingTests(fom));
		}
		return tests;
	}
}