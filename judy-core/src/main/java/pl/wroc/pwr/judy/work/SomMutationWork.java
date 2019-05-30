package pl.wroc.pwr.judy.work;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.client.LocalMutationClient;
import pl.wroc.pwr.judy.common.DurationStatistic;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.general.MutantEvaluator;
import pl.wroc.pwr.judy.hom.som.SomFactory;
import pl.wroc.pwr.judy.hom.som.SomStrategy;
import pl.wroc.pwr.judy.tester.ITesterFactory;
import pl.wroc.pwr.judy.utils.BytecodeCache;
import pl.wroc.pwr.judy.MatrixCoverage;
import pl.wroc.pwr.judy.MatrixExecution;

import java.util.List;

public class SomMutationWork extends MutationWork {
    private static final Logger LOGGER = LogManager.getLogger(SomMutationWork.class);
    private static final long serialVersionUID = 1L;

    private final SomFactory somFactory;
    private String algorithm;

    /**
     * Creates mutation work creating and evaluating second order mutants
     *
     * @param clientId         client id
     * @param retries          number of supported retries
     * @param resultFormatter  maximum source class name length
     * @param targetClass      target class to mutate
     * @param classpath        classpath
     * @param testerFactory    JUnit tester factory
     * @param envFactory       environment factory
     * @param operatorsFactory mutation operators factory
     * @param somFactory       SOM factory
     */
    public SomMutationWork(final long clientId, final int retries, final MutationResultFormatter resultFormatter,
                           final ITargetClass targetClass, final List<String> classpath, final ITesterFactory testerFactory,
                           final IEnvironmentFactory envFactory, final IMutationOperatorsFactory operatorsFactory,
                           final long maxInfiniteLoopGuardTimeout, final SomFactory somFactory, final String algorithm, MatrixExecution MatrixE) {
        super(clientId, retries, resultFormatter, targetClass, classpath, testerFactory, envFactory, operatorsFactory,
                maxInfiniteLoopGuardTimeout, MatrixE);
        this.algorithm = algorithm;
        this.somFactory = somFactory;
    }

    @Override
    protected List<IMutant> mutate() {
        final List<IMutant> foms = super.mutate();;
        if (algorithm.equals(SomStrategy.LAST_TO_FIRST_A.getParam())
                || algorithm.equals(SomStrategy.LAST_TO_FIRST_B.getParam())
                || algorithm.equals(SomStrategy.LAST_TO_FIRST_C.getParam())
                || algorithm.equals(SomStrategy.LAST_TO_FIRST_ABC.getParam())
                || algorithm.equals(SomStrategy.LAST_TO_FIRST_SORTED_ABC.getParam())) {
            LOGGER.debug("Generating FOMs first...");
            this.getEvaluator().evaluate(foms, false);
            this.initReset();
        }
        LOGGER.debug("Generating SOMs...");
        return somFactory.create(foms, getOperators());
    }
}
