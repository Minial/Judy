package pl.wroc.pwr.judy.operators.guards;

import org.junit.Before;
import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;
import resources.operators.guards.DifferentLoopsTestClass;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

@Mutate(operator = ShortTimeoutInfiniteLoopGuard.class, targetClass = DifferentLoopsTestClass.class)
public class InfiniteLoopGuardTest extends AbstractMutationOperatorTesting {

	private InfiniteLoopGuard mutator;

	@Before
	public void setUp() throws Exception {
		mutator = new InfiniteLoopGuard();
	}

	@Test
	public void shouldFindForLoopMutationPoints() throws Exception {
		assertMutationPointsCountEquals(6);
	}

	@Test
	public void shouldCreateMutants() throws Exception {
		assertMutantsCountEquals(6);
	}

	@Test
	public void shouldExitForLoop() throws Exception {
		assertLoopInterrupted(0, "basicLoop");
	}

	@Test
	public void shouldExitWhileLoop() throws Exception {
		assertLoopInterrupted(1, "whileLoop");
	}

	@Test
	public void shouldExitForeachLoop() throws Exception {
		assertLoopInterrupted(2, "foreachLoop");
	}

	@Test
	public void shouldExitDoWhileLoop() throws Exception {
		assertLoopInterrupted(3, "dowhileLoop");
	}

	@Test
	public void shouldExitNestedLoop() throws Exception {
		assertLoopInterrupted(4, "nestedLoop");
	}

	@Test
	public void shouldExitParentOfNestedLoop() throws Exception {
		assertLoopInterrupted(5, "nestedLoopMethodLoop");
	}

	@Test
	public void shouldReturnDescription() throws Exception {
		assertEquals("Introduces escape point in every loop in order to avoid inifinite loop execution",
				mutator.getDescription());
	}

	private void assertLoopInterrupted(int mutantIndex, String methodName) {
		boolean exceptionThrown = false;
		try {
			getMutatedMethodResult(mutantIndex, methodName);
		} catch (InvocationTargetException e) {
			exceptionThrown = true;
			assertTrue(e.getTargetException() instanceof RuntimeException);
			assertEquals("Suspicious loop interrupted by Judy's Infinite Loop Guard", e.getCause().getMessage());
		} catch (Exception e) {
			fail("Unexpected exception " + e.getMessage());
		}
		if (!exceptionThrown) {
			fail("No exception thrown");
		}
	}
}
