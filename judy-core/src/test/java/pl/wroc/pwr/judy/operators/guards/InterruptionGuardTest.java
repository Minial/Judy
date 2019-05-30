package pl.wroc.pwr.judy.operators.guards;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.guards.SleepInterruptTestClass;

import static org.junit.Assert.assertEquals;

@Mutate(operator = InterruptionGuard.class, targetClass = SleepInterruptTestClass.class)
public class InterruptionGuardTest extends AbstractMutationOperatorTesting {

	private InterruptionGuard mutator;

	@Before
	public void setUp() throws Exception {
		mutator = new InterruptionGuard();
	}

	@Test
	public void shouldFindForLoopMutationPoints() throws Exception {
		assertMutationPointsCountEquals(3);
	}

	@Test
	public void shouldCreateMutants() throws Exception {
		assertMutantsCountEquals(3);
	}

	@Test
	public void shouldReturnDescription() throws Exception {
		assertEquals("Intorduces sleep() method call in the beginning of every method "
				+ "in order to provide gentle exit point for test threads interruption", mutator.getDescription());
	}
}
