package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by QV_Nguyen on 14/01/2015.
 */
public class MinTCsNewObjective implements ObjectiveCalculator {
    private String description;
    /*private FragilityCalculator fragilityCalculator;*/

    public MinTCsNewObjective(String description) {
        this.description = description;
        /*this.fragilityCalculator = fragilityCalculator;*/
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
		/*
		* QV_Nguyen Edited
		* Calculate the ratio of #TCs of subset that only kills HOM and cannot kill any FOMs with #TCs which kill HOM
		*/
        Set<String> union = getTestsUnion(foms);
        Set<String> Th = getKillingTests(hom);
        double numberTCsNew = Th.size();
        Th.removeAll(union);
        return numberTCsNew == 0 ? 0 : Th.size()/numberTCsNew;
    }
	/*QV_Nguyen*/


    private static Set<String> getTestsUnion(List<IMutant> foms) {
        Set<String> union = new HashSet<>();

        for (IMutant fom : foms) {

                union.addAll(getKillingTests(fom));
        }
        return union;
    }
    private static Set<String> getKillingTests(IMutant mutant) {
        return mutant.getResults().getFailingTestMethods();
    }

   /* private static Set<String> getKillingTests(List<IMutant> foms) {
        Set<String> tests = new LinkedHashSet<>();
        for (IMutant fom : foms) {
            tests.addAll(getKillingTests(fom));
        }
        return tests;
    }*/ /*QV_Nguyen*/

    @Override
    public double getWorstValue() {
        return WORST_POSITIVE_VALUE;
    }

    @Override
    /*public boolean isValuable(double value) {
        return value >= 0 && value <= 1;
    }*/
    public boolean isValuable(double value) {
        return true;
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
        if (!(obj instanceof MinTCsNewObjective)) {
            return false;
        }
        MinTCsNewObjective other = (MinTCsNewObjective) obj;
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
