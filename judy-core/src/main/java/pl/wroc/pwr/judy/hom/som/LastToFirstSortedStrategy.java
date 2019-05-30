package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * LastToFirst strategy for generating SOMs. Combining first order mutants in a
 * specific order: last mutant with the first in the list, one before last with
 * the second, and so on. Every first order mutant is used once, except the
 * situation when list of mutants is odd. Then one of the first order mutants is
 * used twice.
 */
public class LastToFirstSortedStrategy extends AbstractOrderStrategy {
    @Override
    public List<IMutant> create(List<IMutant> orginFoms, List<IMutationOperator> operators) {
        sort(orginFoms);
        return doMutant(orginFoms, operators);
    }

    @Override
    protected int getNextFomIndex(List<IMutant> foms) {
        return foms.size() - 1;
    }

    private void sort(List<IMutant> mutants) {
        mutants.sort((first, second) -> {
            double firstValue = 0;
            double secondValue = 0;
            if (first.getResults().getTestMethods() != null && first.getResults().getTestMethods().size() != 0) {
                if (first.getResults().getFailingTestMethods() != null && first.getResults().getFailingTestMethods().size() != 0) {
                    firstValue = 100.0 * first.getResults().getFailingTestMethods().size()
                            / first.getResults().getTestMethods().size();
                }
            }

            if (second.getResults().getTestMethods() != null && second.getResults().getTestMethods().size() != 0) {
                if (second.getResults().getFailingTestMethods() != null && second.getResults().getFailingTestMethods().size() != 0) {
                    secondValue = 100.0 * second.getResults().getFailingTestMethods().size()
                            / second.getResults().getTestMethods().size();
                }
            }
            return firstValue > secondValue ? 1 : -1;
        });
    }



}
