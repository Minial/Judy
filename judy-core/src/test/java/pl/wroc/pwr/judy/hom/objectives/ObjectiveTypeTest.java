package pl.wroc.pwr.judy.hom.objectives;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectiveTypeTest {
	@Test
	public void shouldFindObjectiveByName() throws Exception {
		ObjectiveType o = ObjectiveType.lookup(ObjectiveType.FITNESS.getName());
		assertNotNull(o);
		assertEquals(ObjectiveType.FITNESS, o);
	}

	@Test
	public void shouldReturnNullForUnknownObjectiveName() throws Exception {
		ObjectiveType o = ObjectiveType.lookup("dummyName");
		assertNull(o);
	}

	@Test
	public void shouldReturnOperatorCalculatorInstance() throws Exception {
		assertTrue(ObjectiveType.FITNESS.getInstance() instanceof FitnessObjective);
		assertTrue(ObjectiveType.MUTATION_ORDER.getInstance() instanceof MutationOrderObjective);
		assertTrue(ObjectiveType.MAX_OPERATOR_DIVERSITY.getInstance() instanceof MaxOperatorDiversityObjective);
		assertTrue(ObjectiveType.MIN_OPERATOR_DIVERSITY.getInstance() instanceof MinOperatorDiversityObjective);
	}

	@Test
	public void shouldReturnOperatorName() throws Exception {
		assertEquals("fitness", ObjectiveType.FITNESS.getName());
		assertEquals("mutationOrder", ObjectiveType.MUTATION_ORDER.getName());
		assertEquals("maxOperatorDiversity", ObjectiveType.MAX_OPERATOR_DIVERSITY.getName());
		assertEquals("minOperatorDiversity", ObjectiveType.MIN_OPERATOR_DIVERSITY.getName());
	}
}
