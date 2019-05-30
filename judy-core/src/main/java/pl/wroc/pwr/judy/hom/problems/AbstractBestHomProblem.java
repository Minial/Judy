package pl.wroc.pwr.judy.hom.problems;

import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.general.IMutantEvaluator;
import pl.wroc.pwr.judy.hom.HomFactory;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveType;
import pl.wroc.pwr.judy.hom.research.IHomDataCollector;

import java.util.*;

public abstract class  AbstractBestHomProblem extends AbstractProblem {
	protected static final int DECISION_VARIABLES = 1;
	protected static final int MIN_CARDINALITY = 2;
	private List<IMutationOperator> operators;
	private IMutantEvaluator evaluator;
	private List<ObjectiveCalculator> calculators;
	private HomFactory homFactory;
	private IHomDataCollector dataCollector;
	private int maxMutationOrder;
	protected Map<BitSet, List<Double>> evaluatedHomsCache;

	/**
	 * Creates best HOM problem, with one decision variable and number of
	 * objectives according to ObjectiveCalcuators settings. Calls super
	 * constructor of AbstractProblem class from MOEA framework.
	 *
	 * @param operators        mutation operators
	 * @param evaluator        mutant evaluator
	 * @param calculators      HOM objectives value calculators
	 * @param homFactory       factory creating HOMs
	 * @param dataCollector    HOM research data collector
	 * @param maxMutationOrder maximum mutation order of generated mutants
	 */
	public AbstractBestHomProblem(List<IMutationOperator> operators, IMutantEvaluator evaluator,
								  List<ObjectiveCalculator> calculators, HomFactory homFactory, IHomDataCollector dataCollector,
								  int maxMutationOrder) {
		super(DECISION_VARIABLES, calculators.size());
		this.operators = operators;
		this.evaluator = evaluator;
		this.calculators = calculators;
		this.homFactory = homFactory;
		this.dataCollector = dataCollector;
		this.maxMutationOrder = maxMutationOrder;
		evaluatedHomsCache = new HashMap<>();
	}

	/**
	 * Assignes worst value for each objective
	 *
	 * @param solution - to assing objectives
	 */
	protected void penalize(Solution solution) {
		for (int i = 0; i < getCalculators().size(); i++) {
			solution.setObjective(i, getCalculators().get(i).getWorstValue());
		}
	}

	/**
	 * Calculates MutationOrderObjective if it is used, penalizes on other
	 * objectives
	 *
	 * @param solution - to assing objectives
	 */
	protected void penalizeTowardsValidCardinality(Solution solution, int cardinality) {
		for (int i = 0; i < getCalculators().size(); i++) {
			String calculatorDescription = getCalculators().get(i).getDescription();
			if (calculatorDescription != null && calculatorDescription.equals(ObjectiveType.MUTATION_ORDER.getName())) {
				solution.setObjective(i, getCalculators().get(i).calculate(null, null, cardinality));
			} else {
				solution.setObjective(i, getCalculators().get(i).getWorstValue());
			}
		}
	}

	protected List<Double> calculateObjectives(IMutant hom, List<IMutant> usedFoms, Solution solution, int order) {
		List<Double> objectivesValues = new ArrayList<>(getCalculators().size());
		for (int i = 0; i < getCalculators().size(); i++) {
			double objectiveValue = getCalculators().get(i).calculate(hom, usedFoms, order);
			solution.setObjective(i, objectiveValue);
			objectivesValues.add(objectiveValue);
		}
		return objectivesValues;
	}

	protected void setObjectivesFromCache(BitSet bs, Solution solution) {
		List<Double> objectives = evaluatedHomsCache.get(bs);
		for (int i = 0; i < objectives.size(); i++) {
			solution.setObjective(i, objectives.get(i));
		}
	}

	protected void cacheHomEvaluation(BitSet bs, List<Double> objectiveValues) {
		evaluatedHomsCache.put(bs, objectiveValues);
	}

	/**
	 * @return the operators
	 */
	public List<IMutationOperator> getOperators() {
		return operators;
	}

	/**
	 * @return the evaluator
	 */
	public IMutantEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * @return the calculators
	 */
	public List<ObjectiveCalculator> getCalculators() {
		return calculators;
	}

	/**
	 * @return the homFactory
	 */
	public HomFactory getHomFactory() {
		return homFactory;
	}

	/**
	 * @return the dataCollector
	 */
	public IHomDataCollector getDataCollector() {
		return dataCollector;
	}

	/**
	 * Gets maximum mutation order limit for HOM generation
	 *
	 * @return maximum mutation order
	 */
	public int getMaxMutationOrder() {
		return maxMutationOrder;
	}
}
