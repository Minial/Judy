package pl.wroc.pwr.judy.hom.problems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.general.IMutantEvaluator;
import pl.wroc.pwr.judy.hom.HomFactory;
import pl.wroc.pwr.judy.hom.IncompatibleMutationException;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.hom.research.IHomDataCollector;

import pl.wroc.pwr.judy.hom.som.ClosePairStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * Best HOM problem implementation using binary representation of HOM - every
 * known FOM is represented by one bit. All possible FOMs are generated and
 * evaluated a priori to HOM generation and evaluation.
 *
 * @author TM
 */
public class UpFrontBestHomProblem extends AbstractBestHomProblem {
	private static final Logger LOGGER = LogManager.getLogger(UpFrontBestHomProblem.class);

	private List<IMutant> foms;

	/**
	 * Creates HOM fitness problem, using objective calculators settings
	 * (objectives) and one decision variable - binary representation of FOMs
	 * selection for HOM generation. For testing purposes.
	 *
	 * @param operators        mutation operators
	 * @param evaluator        mutant evaluator
	 * @param calculators      HOM objectives value calculators
	 * @param homFactory       HOM factory
	 * @param foms             first order mutants, FOMs
	 * @param dataCollector    HOM research data collector
	 * @param maxMutationOrder maximum mutation order of generated mutants
	 */
	public UpFrontBestHomProblem(List<IMutationOperator> operators, IMutantEvaluator evaluator,
								 List<ObjectiveCalculator> calculators, HomFactory homFactory, List<IMutant> foms,
								 IHomDataCollector dataCollector, int maxMutationOrder) {
		super(operators, evaluator, calculators, homFactory, dataCollector, maxMutationOrder);
		this.foms = foms;

	}

	@Override
	public void evaluate(Solution solution) {
		BitSet bs = EncodingUtils.getBitSet(solution.getVariable(0));
		if (bs.cardinality() >= MIN_CARDINALITY && bs.cardinality() <= getMaxMutationOrder()) {
			List<IMutant> usedFoms = getUsedFoms(bs);
						try {
				// avoid duplicates
				if (evaluatedHomsCache.containsKey(bs)) {
					setObjectivesFromCache(bs, solution);
				} else {
					// SOMT firstly
					List<IMutant> usedSoms = getUsedSoms(usedFoms); //QVN
					IMutant hom = generateHom(usedSoms); //QVN
					//IMutant hom = generateHom(usedFoms);
					evaluateHom(hom);
					List<Double> objectiveValues = calculateObjectives(hom, usedFoms, solution, bs.cardinality());
					getDataCollector().log(hom, usedFoms, solution);
					cacheHomEvaluation(bs, objectiveValues);
				}
			} catch (IncompatibleMutationException e) {
				LOGGER.debug("Incompatible mutation  ", e);
				penalize(solution);
			}
		} else {
			penalizeTowardsValidCardinality(solution, bs.cardinality());
		}
	}

	private void evaluateHom(IMutant hom) {
		getEvaluator().evaluate(Arrays.asList(hom), true);
	}

	private IMutant generateHom(List<IMutant> foms) throws IncompatibleMutationException {
		IMutant hom = getHomFactory().create(foms, getOperators());
		return hom;
	}

	private List<IMutant> getUsedFoms(BitSet binary) {
		List<IMutant> used = new ArrayList<>(binary.cardinality());
		for (int i = binary.nextSetBit(0); i >= 0; i = binary.nextSetBit(i + 1)) {
			used.add(foms.get(i));
		}
		return used;
	}
//QVN Edited - SOMT
	private List<IMutant> getUsedSoms(List<IMutant> usedFoms) {
		//List<IMutant> usedSoms = null;
		List<IMutationOperator> OP = getOperators();
		ClosePairStrategy closePairStrategy =null;
		List<IMutant> used = closePairStrategy.create(usedFoms, OP); ///Exception error!!!

	return used;//End edit
}
	@Override
	public Solution newSolution() {
		Solution s = new Solution(getNumberOfVariables(), getNumberOfObjectives());
		BinaryVariable binaryVariable = new BinaryVariable(foms.size());
		s.setVariable(0, binaryVariable);
		return s;
	}
}
