package pl.wroc.pwr.judy.work;

import org.junit.Before;
import org.mockito.Mockito;
import pl.wroc.pwr.cluster.IWork;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IEnvironmentFactory;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.tester.ITesterFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public abstract class AbstractMutationWorkFactoryTest {
	public static final int ID = 1;
	protected List<String> classpath;
	protected ITesterFactory testerFactory;
	protected IMutationOperatorsFactory operatorsFactory;
	protected ITargetClass targetClass;
	protected IEnvironmentFactory envFactory;
	protected MutationResultFormatter resultFormatter;

	protected void assertMutationWorkDetails(final ITargetClass targetClass, final IWork work) {
		final MutationWork mutationWork = (MutationWork) work;
		assertEquals(targetClass, mutationWork.getTargetClass());
	}

	protected void checkWorkBasics(final IWork work) {
		assertNotNull(work);
		assertEquals(ID, work.getClientId());
		assertFalse(work.canRetry());
	}

	@Before
	public void setUp() throws Exception {
		classpath = new ArrayList<>();
		testerFactory = Mockito.mock(ITesterFactory.class);
		operatorsFactory = Mockito.mock(IMutationOperatorsFactory.class);
		resultFormatter = Mockito.mock(MutationResultFormatter.class);

		targetClass = Mockito.mock(ITargetClass.class);
		envFactory = Mockito.mock(IEnvironmentFactory.class);
	}

}
