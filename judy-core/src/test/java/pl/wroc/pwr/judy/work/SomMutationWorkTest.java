package pl.wroc.pwr.judy.work;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.hom.som.SomFactory;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SomMutationWorkTest {
	private static final String TARGET_CLASS = "tc";

	private SomMutationWork work;
	private SomFactory generator;
	private List<IMutationOperator> operators;
	private List<IMutant> foms;
	private MutationResultFormatter resultFormatter;

	@Before
	public void setUp() throws Exception {
		generator = Mockito.mock(SomFactory.class);
		foms = new ArrayList<>();
		operators = new ArrayList<>();
		resultFormatter = Mockito.mock(MutationResultFormatter.class);

		final ITargetClass targetClass = mockTargetClass();
		work = new SomMutationWork(0l, 0, resultFormatter, targetClass, new ArrayList<String>(), null, null, null,
				InfiniteLoopGuard.DEFAULT_TIMEOUT, generator, "") {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<IMutant> generateFoms(final byte[] bytecode) {
				return foms;
			}
		};
	}

	@Test
	public void shouldCreateSomsUsingProvidedAlgorith() throws Exception {
		final IBytecodeCache cache = Mockito.mock(IBytecodeCache.class);
		Mockito.when(cache.get(TARGET_CLASS)).thenReturn(new byte[]{0, 0, 0});
		work.setCache(cache);
		work.setOperators(new ArrayList<IMutationOperator>());

		final List<IMutant> mutants = work.mutate();
		assertNotNull(mutants);
		assertTrue(mutants.isEmpty());

		Mockito.verify(generator).create(foms, operators);
	}

	private ITargetClass mockTargetClass() {
		final ITargetClass tc = Mockito.mock(ITargetClass.class);
		Mockito.when(tc.getName()).thenReturn(TARGET_CLASS);
		return tc;
	}
}
