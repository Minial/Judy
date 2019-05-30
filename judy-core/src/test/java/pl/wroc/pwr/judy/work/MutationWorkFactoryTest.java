package pl.wroc.pwr.judy.work;

import org.junit.Test;
import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;

import static org.junit.Assert.assertTrue;

public class MutationWorkFactoryTest extends AbstractMutationWorkFactoryTest {

	@Test
	public void shouldCreateMutationWork() throws Exception {
		final IWork work = createWork();
		checkWorkBasics(work);

		assertTrue(work instanceof MutationWork);
		assertMutationWorkDetails(targetClass, work);
	}

	private IWork createWork() {
		final MutationWorkFactory factory = new MutationWorkFactory(0, resultFormatter, classpath, testerFactory,
				operatorsFactory, InfiniteLoopGuard.DEFAULT_TIMEOUT);
		return factory.create(ID, targetClass, envFactory);
	}
}
