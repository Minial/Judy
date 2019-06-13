package pl.wroc.pwr.judy.work;

import org.moeaframework.Executor;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.HomConfig;
import pl.wroc.pwr.judy.hom.filter.IMutantFilter;
import pl.wroc.pwr.judy.hom.problems.UpFrontBestHomProblem;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

/**
 * HOM mutation work generating all possible FOMs up front (using
 * UpFrontBestHomProblem as a problem definition).
 *
 * @author tmuc
 */
public class UpFrontHomMutationWork extends HomMutationWork {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates mutation work creating and evaluating high order mutants
	 *
	 * @param clientId         client id
	 * @param retries          number of supported retries
	 * @param resultFormatter  mutation result formatter
	 * @param targetClass      target class to mutate
	 * @param classpath        classpath
	 * @param testerFactory    JUnit tester factory
	 * @param envFactory       environment factory
	 * @param operatorsFactory mutation operators factory
	 * @param config           HOM mutation config
	 * @param filter           FOM mutants filter
	 * @param MatrixE		   MatrixExecution
	 */
	public UpFrontHomMutationWork(long clientId, int retries, MutationResultFormatter resultFormatter,
								  ITargetClass targetClass, List<String> classpath, ITesterFactory testerFactory,
								  IEnvironmentFactory envFactory, IMutationOperatorsFactory operatorsFactory, long infiniteLoopBreakTime,
								  HomConfig config, IMutantFilter filter, MatrixExecution MatrixE, MatrixCoverage MatrixC) {
		super(clientId, retries, resultFormatter, targetClass, classpath, testerFactory, envFactory, operatorsFactory,
				infiniteLoopBreakTime, config, filter, MatrixE, MatrixC);
	}

	private List<IMutant> generateAndEvaluateFoms() {
		List<IMutant> foms = mutate();
		getEvaluator().evaluate(foms, false);
		return foms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Executor createExecutor() {
		List<IMutant> foms = getFilter().filter(generateAndEvaluateFoms());
		return new Executor().withProblemClass(UpFrontBestHomProblem.class, getOperators(), getEvaluator(),
				getObjectives(), createHomFactory(), foms, createDataCollector(), getMaxMutationOrder());
	}

}
