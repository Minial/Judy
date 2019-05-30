package pl.wroc.pwr.judy.hom.objectives;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObjectivesFactoryTest {
	private ObjectivesFactory factory;

	@Test
	public void shouldReturnAllObjectivesForNullNames() throws Exception {
		factory = new ObjectivesFactory(null);
		checkBasicObjectives(factory.createObjectives());
	}

	@Test
	public void shouldReturnAddObjectivesForEmptyNamesList() throws Exception {
		factory = new ObjectivesFactory(new ArrayList<String>());
		checkBasicObjectives(factory.createObjectives());
	}

	@Test
	public void shouldReturnConfiguredObjectivesOnly() throws Exception {
		factory = new ObjectivesFactory(Arrays.asList(ObjectiveType.FITNESS.getName(),
				ObjectiveType.MUTATION_ORDER.getName()));

		List<ObjectiveCalculator> objectives = factory.createObjectives();
		assertEquals(2, objectives.size());
		assertTrue(objectives.get(0) instanceof FitnessObjective);
		assertTrue(objectives.get(1) instanceof MutationOrderObjective);
	}

	@Test
	public void shouldIgnoreDuplicatedObjectiveNames() throws Exception {
		factory = new ObjectivesFactory(Arrays.asList(ObjectiveType.FITNESS.getName(), ObjectiveType.FITNESS.getName()));
		assertEquals(1, factory.createObjectives().size());
	}

	@Test
	public void shouldReturnBasicObjectivesIfAllNamesAreInvalid() throws Exception {
		factory = new ObjectivesFactory(Arrays.asList("dummyName", "dummyName2"));
		List<ObjectiveCalculator> objectives = factory.createObjectives();
		checkBasicObjectives(objectives);
	}

	private void checkBasicObjectives(List<ObjectiveCalculator> objectives) {
		assertEquals(2, objectives.size());
		assertTrue(objectives.contains(ObjectiveType.FITNESS.getInstance()));
		assertTrue(objectives.contains(ObjectiveType.MUTATION_ORDER.getInstance()));
	}

}
