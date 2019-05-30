package pl.wroc.pwr.judy.work;

import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.IMutationWorkFactory;
import pl.wroc.pwr.judy.hom.som.ISomGeneratorFactory;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;

import static org.junit.Assert.assertTrue;

public class SomMutationWorkFactoryTest extends AbstractMutationWorkFactoryTest {
	@Test
	public void shouldCreateSomMutationWork() throws Exception {
		final IWork work = createWork(Mockito.mock(ISomGeneratorFactory.class), null);
		checkWorkBasics(work);

		assertTrue(work instanceof SomMutationWork);
		assertMutationWorkDetails(targetClass, work);
	}

	private IWork createWork(final ISomGeneratorFactory somGeneratorFactory, IMutationWorkFactory homFactory) {
		final SomMutationWorkFactory factory = new SomMutationWorkFactory(0, resultFormatter, classpath, testerFactory,
				operatorsFactory, InfiniteLoopGuard.DEFAULT_TIMEOUT, somGeneratorFactory);
		return factory.create(ID, targetClass, envFactory);
	}
}
