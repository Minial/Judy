package pl.wroc.pwr.judy.work;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.BaseHomFactory;
import pl.wroc.pwr.judy.hom.HomConfig;
import pl.wroc.pwr.judy.hom.HomFactory;
import pl.wroc.pwr.judy.hom.UniqueLinesHomFactory;
import pl.wroc.pwr.judy.hom.filter.IMutantFilter;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;
import pl.wroc.pwr.judy.hom.research.HomDataCollector;
import pl.wroc.pwr.judy.hom.research.IHomDataCollector;
import pl.wroc.pwr.judy.hom.utils.CustomInitializationAlgorithmFactory;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

public abstract class HomMutationWork extends MutationWork {
	private static final long serialVersionUID = 1L;

	private List<ObjectiveCalculator> objectives;
	private int maxEvaluations;
	private boolean skipSameLineMutations;
	private int maxMutationOrder;
	private IMutantFilter filter;
	public MatrixExecution MatrixE;

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
	 * @param MatrixE		   Execution Matrix
	 */
	public HomMutationWork(long clientId, int retries, MutationResultFormatter resultFormatter,
						   ITargetClass targetClass, List<String> classpath, ITesterFactory testerFactory,
						   IEnvironmentFactory envFactory, IMutationOperatorsFactory operatorsFactory,
						   long maxInfiniteLoopGuardTimeout, HomConfig config, IMutantFilter filter, MatrixExecution MatrixE) {
		super(clientId, retries, resultFormatter, targetClass, classpath, testerFactory, envFactory, operatorsFactory,
				maxInfiniteLoopGuardTimeout, MatrixE);
		maxEvaluations = config.getMaxEvaluations();
		objectives = config.getObjectivesFactory().createObjectives();
		skipSameLineMutations = config.isSkippingSameLineMutations();
		maxMutationOrder = config.getMaxMutationOrder();
		this.filter = filter;
	}

	@Override
	public IClassMutationResult run(Object workspace) throws Exception {
		init((String) workspace);

		searchBestHom(maxEvaluations);

		return new ClassMutationResult(getKind(), getTargetClass(), getEvaluator().getAliveMutants(), getEvaluator()
				.getKilledMutants(), getStatistic(), getResultFormatter().getSummary(collectSummaryData(),
				getTargetClass(), getKind(), getStatistic()));
	}

	private NondominatedPopulation searchBestHom(int maxEvaluations) {
		return getExecutor(maxEvaluations).run();
	}

	protected HomFactory createHomFactory() {
		return skipSameLineMutations ? new UniqueLinesHomFactory(getStatistic()) : new BaseHomFactory(getStatistic());
	}

	protected IHomDataCollector createDataCollector() {
		return new HomDataCollector(objectives);
	}

	protected List<ObjectiveCalculator> getObjectives() {
		return objectives;
	}

	protected Executor getExecutor(int maxEvaluations) {
		return createExecutor().usingAlgorithmFactory(CustomInitializationAlgorithmFactory.getInstance())
				.withAlgorithm("NSGAII_CustomInitialization") .withProperty("maxMutationOrder", maxMutationOrder)
				.withMaxEvaluations(maxEvaluations);
	}

	protected IMutantFilter getFilter() {
		return filter;
	}

	protected int getMaxMutationOrder() {
		return maxMutationOrder;
	}

	protected abstract Executor createExecutor();
}
