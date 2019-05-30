package pl.wroc.pwr.judy.work;

import org.moeaframework.Executor;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.CachingFomFactory;
import pl.wroc.pwr.judy.hom.FomFactory;
import pl.wroc.pwr.judy.hom.HomConfig;
import pl.wroc.pwr.judy.hom.filter.IMutantFilter;
import pl.wroc.pwr.judy.hom.problems.NotEquivalentBestHomProblem;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

/**
 * HOM mutation work generating NON equivalent FOMs, only when those needed for HOM
 * generation
 *
 * @author QVN
 */
public class NotEquivalentHomMutationWork extends HomMutationWork {
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
     * @param MatrixE		   Execution Matrix
     */
    public NotEquivalentHomMutationWork(long clientId, int retries, MutationResultFormatter resultFormatter,
                                   ITargetClass targetClass, List<String> classpath, ITesterFactory testerFactory,
                                   IEnvironmentFactory envFactory, IMutationOperatorsFactory operatorsFactory, long infiniteLoopBreakTime,
                                   HomConfig config, IMutantFilter filter, MatrixExecution MatrixE) {
        super(clientId, retries, resultFormatter, targetClass, classpath, testerFactory, envFactory, operatorsFactory,
                infiniteLoopBreakTime, config, filter, MatrixE);
    }

    private FomFactory createFomFactory(final byte[] bytecode) {
        return new CachingFomFactory(bytecode, getTargetClass().getName(), getOperators(), getStatistic());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Executor createExecutor() {
        final byte[] bytecode = getBytecode();

        return new Executor().withProblemClass(NotEquivalentBestHomProblem.class, getOperators(), getEvaluator(),
                getObjectives(), createHomFactory(), createFomFactory(bytecode), bytecode, createDataCollector(),
                getMaxMutationOrder(), getFilter());
    }
}
