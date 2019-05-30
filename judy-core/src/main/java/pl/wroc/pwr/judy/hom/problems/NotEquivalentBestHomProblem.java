package pl.wroc.pwr.judy.hom.problems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationPoint;
import pl.wroc.pwr.judy.general.IMutantEvaluator;
import pl.wroc.pwr.judy.hom.FomFactory;
import pl.wroc.pwr.judy.hom.HomFactory;
import pl.wroc.pwr.judy.hom.IncompatibleMutationException;
import pl.wroc.pwr.judy.hom.MutationInfo;
import pl.wroc.pwr.judy.hom.filter.IMutantFilter;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.hom.research.IHomDataCollector;

import java.util.*;

/**
 * Best HOM problem implementation using binary representation of HOM - every
 * possible operator usage is represented by one bit. Non-Equivalent FOMs used for HOM generation and
 * evaluation.
 *
 * @author QVN
 */
public class NotEquivalentBestHomProblem extends AbstractBestHomProblem {
    private static final Logger LOGGER = LogManager.getLogger(NotEquivalentBestHomProblem.class);

    private FomFactory fomFactory;
    private byte[] bytecode;
    private IMutantFilter filter;
    private List<MutationInfo> possibleMutations;

    /**
     * Creates HOM fitness problem, using objective calculators settings
     * (objectives) and one decision variable - binary representation of FOMs
     * selection for HOM generation.
     *
     * @param operators        mutation operators
     * @param evaluator        mutant evaluator
     * @param calculators      HOM objectives value calculators
     * @param homFactory       factory creating HOMs
     * @param fomFactory       factory creating FOMs out of target class' bytecode
     * @param bytecode         bytecode of target class being analyzed
     * @param dataCollector    HOM research data collector
     * @param maxMutationOrder maximum mutation order of generated mutants
     */
    public NotEquivalentBestHomProblem(List<IMutationOperator> operators, IMutantEvaluator evaluator,
                                  List<ObjectiveCalculator> calculators, HomFactory homFactory, FomFactory fomFactory, byte[] bytecode,
                                  IHomDataCollector dataCollector, int maxMutationOrder, IMutantFilter filter) {
        super(operators, evaluator, calculators, homFactory, dataCollector, maxMutationOrder);
        this.fomFactory = fomFactory;
        this.bytecode = bytecode;
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evaluate(Solution solution) {
        BitSet bs = EncodingUtils.getBitSet(solution.getVariable(0));
        if (bs.cardinality() >= MIN_CARDINALITY && bs.cardinality() <= getMaxMutationOrder()) {
            // check for duplicated solutions during genetic algorithm
            // iterations
            if (evaluatedHomsCache.containsKey(bs)) {
                setObjectivesFromCache(bs, solution);
            } else {
                try {
                    List<IMutant> foms = new ArrayList<>(bs.cardinality());
                    for (MutationInfo mi : getUsedMutations(bs)) {
                        IMutant fom = fomFactory.createFom(mi);
                        if (fom != null) {
                            foms.add(fom);
                        }
                    }
                    evaluateFoms(foms);
                    if (allFomsArePassingFiltration(foms)) {
                        /* Get FOMs which are not equivalent mutants getKillingTests(mutant).size()!= 0*/
                        /* Get FOMs which are not easy to kill getKillingTests(mutant).size()< Easy*/
                        double Easy = getKillingTests(foms).size();
                        List<IMutant> getfoms = new ArrayList<>(bs.cardinality());
                        for (IMutant mutant : foms) {
                            if (getKillingTests(mutant).size()< Easy) {
                                getfoms.add(mutant);
                            }
                        }
                        /* QVN edited*/
                        IMutant hom = generateHom(getfoms);
                        evaluateHom(hom);

                        List<Double> objectiveValues = calculateObjectives(hom, foms, solution, bs.cardinality());
                        getDataCollector().log(hom, foms, solution);
                        cacheHomEvaluation(bs, objectiveValues);
                    } else {
                        penalize(solution);
                    }
                } catch (IncompatibleMutationException e) {
                    LOGGER.debug("Incompatible mutation  ", e);
                    penalize(solution);
                }
            }
        } else {
            penalizeTowardsValidCardinality(solution, bs.cardinality());
        }
    }

    private boolean allFomsArePassingFiltration(List<IMutant> mutants) {
        return mutants.size() == filter.filter(mutants).size();
    }

    private List<MutationInfo> getUsedMutations(BitSet bs) {
        List<MutationInfo> used = new ArrayList<>(bs.cardinality());
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            used.add(getPossibleMutations().get(i));
        }
        return used;
    }

    private void evaluateFoms(List<IMutant> foms) {
        getEvaluator().evaluate(extractNotEvaluatedFoms(foms), false);
    }

    private List<IMutant> extractNotEvaluatedFoms(List<IMutant> foms) {
        ArrayList<IMutant> notEvaluated = new ArrayList<>();
        for (IMutant mutant : foms) {
            if (mutant.getResults().isEmpty()) {
                notEvaluated.add(mutant);
            }
        }
        notEvaluated.trimToSize();
        return notEvaluated;
    }

    private IMutant generateHom(List<IMutant> foms) throws IncompatibleMutationException {
        IMutant hom = getHomFactory().create(foms, getOperators());
        return hom;
    }

    private void evaluateHom(IMutant hom) {
        getEvaluator().evaluate(Arrays.asList(hom), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Solution newSolution() {
        Solution s = new Solution(getNumberOfVariables(), getNumberOfObjectives());
        BinaryVariable binaryVariable = new BinaryVariable(getPossibleMutations().size());
        s.setVariable(0, binaryVariable);
        return s;
    }

    protected List<MutationInfo> getPossibleMutations() {
        if (possibleMutations == null) {
            possibleMutations = loadMutations();
        }
        return possibleMutations;
    }

    private List<MutationInfo> loadMutations() {
        ArrayList<MutationInfo> mutations = new ArrayList<>();

        for (IMutationOperator operator : getOperators()) {
            for (IMutationPoint point : operator.getMutationPoints(bytecode)) {
                for (int mutantIndex = 0; mutantIndex < point.getMutantsCount(); mutantIndex++) {
                    mutations.add(new MutationInfo(operator, point.getIndex(), mutantIndex));
                }
            }
        }
        mutations.trimToSize();
        return mutations;
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
    } /*QV_Nguyen*/
}
