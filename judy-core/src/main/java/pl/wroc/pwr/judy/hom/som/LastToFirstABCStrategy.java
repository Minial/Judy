package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.hom.MutationInfo;
import pl.wroc.pwr.judy.operators.MutationPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * LastToFirst strategy for generating SOMs. Combining first order mutants in a
 * specific order: last mutant with the first in the list, one before last with
 * the second, and so on. Every first order mutant is used once, except the
 * situation when list of mutants is odd. Then one of the first order mutants is
 * used twice.
 */
public class LastToFirstABCStrategy extends AbstractOrderStrategy {

    @Override
    public List<IMutant> create(List<IMutant> orginFoms, List<IMutationOperator> operators) {

        List<IMutant> fomsA = new ArrayList<>();
        List<IMutant> fomsB = new ArrayList<>();
        List<IMutant> fomsC = new ArrayList<>();
        for (IMutant mutant : orginFoms) {
            if (mutant.getResults().getTestMethods() != null && mutant.getResults().getTestMethods().size() != 0) {
                if (mutant.getResults().getFailingTestMethods() != null && mutant.getResults().getFailingTestMethods().size() != 0) {
                    double percent = 100.0 * mutant.getResults().getFailingTestMethods().size()
                            / mutant.getResults().getTestMethods().size();
                    if (percent >= 70) {
                        fomsA.add(mutant);
                    } else if (percent >= 30) {
                        fomsB.add(mutant);
                    } else {
                        fomsC.add(mutant);
                    }
                } else {
                    fomsC.add(mutant);
                }
            }
        }

        List<IMutant> mutants = new ArrayList<>(doMutant(fomsA, operators));
        mutants.addAll(doMutant(fomsB, operators));
        mutants.addAll(doMutant(fomsC, operators));
        return mutants;
    }

    @Override
    protected int getNextFomIndex(List<IMutant> foms) {
        return foms.size() - 1;
    }
}
