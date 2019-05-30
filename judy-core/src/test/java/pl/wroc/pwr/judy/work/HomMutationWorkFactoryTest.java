package pl.wroc.pwr.judy.work;

import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.hom.HomConfig;
import pl.wroc.pwr.judy.hom.HomStrategy;
import pl.wroc.pwr.judy.hom.objectives.IObjectivesFactory;
import pl.wroc.pwr.judy.hom.som.ISomGeneratorFactory;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;

import static org.junit.Assert.assertTrue;

public class HomMutationWorkFactoryTest extends AbstractMutationWorkFactoryTest {

	@Test
	public void shouldCreateUpFrontHomMutationWork() throws Exception {
		final IWork work = createWork(null, Mockito.mock(IObjectivesFactory.class), HomStrategy.UP_FRONT);
		checkWorkBasics(work);

		assertTrue(work instanceof UpFrontHomMutationWork);
		assertMutationWorkDetails(targetClass, work);
	}

	private IWork createWork(final ISomGeneratorFactory somGeneratorFactory, IObjectivesFactory homObjectiveFactory,
							 HomStrategy homStrategy) {
		final HomMutationWorkFactory factory = new HomMutationWorkFactory(0, resultFormatter, classpath, testerFactory,
				operatorsFactory, InfiniteLoopGuard.DEFAULT_TIMEOUT, createConfig(homObjectiveFactory, homStrategy));
		return factory.create(ID, targetClass, envFactory);
	}

	private HomConfig createConfig(IObjectivesFactory homObjectiveFactory, HomStrategy homStrategy) {
		HomConfig cfg = new HomConfig();
		cfg.setObjectivesFactory(homObjectiveFactory);
		cfg.setStrategy(homStrategy);
		return cfg;
	}

	@Test
	public void shouldCreateOnTheFlyHomMutationWork() throws Exception {
		final IWork work = createWork(null, Mockito.mock(IObjectivesFactory.class), HomStrategy.ON_THE_FLY);
		checkWorkBasics(work);

		assertTrue(work instanceof OnTheFlyHomMutationWork);
		assertMutationWorkDetails(targetClass, work);
	}
}
