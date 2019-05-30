package pl.wroc.pwr.judy.hom.objectives;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ObjectivesFactory implements IObjectivesFactory {
	private static final Logger LOGGER = LogManager.getLogger(IObjectivesFactory.class);

	private Set<ObjectiveCalculator> objectives = new LinkedHashSet<>();

	/**
	 * Creates objective factory using passed objective names
	 *
	 * @param names objective names (params)
	 */
	public ObjectivesFactory(List<String> names) {
		if (!hasAnyObjectiveSet(names)) {
			addBasicObjectives();
		} else {
			addSelectedObjectives(names);
		}
	}

	private void addSelectedObjectives(List<String> names) {
		for (String name : names) {
			ObjectiveType objective = ObjectiveType.lookup(name);
			if (objective != null) {
				addObjective(objective);
			} else {
				LOGGER.debug("Skipping unknown HOM objective: " + name);
			}
		}
		if (objectives.isEmpty()) {
			addBasicObjectives();
		}
	}

	private void addBasicObjectives() {
		addObjective(ObjectiveType.MIN_TCsSSHOM);
		addObjective(ObjectiveType.MIN_TCsNew);
		addObjective(ObjectiveType.MIN_TCsOld);
		addObjective(ObjectiveType.FITNESS);
		addObjective(ObjectiveType.MUTATION_ORDER);
	}

	private void addObjective(ObjectiveType o) {
		LOGGER.info("Adding HOM objective: " + o.getName());
		objectives.add(o.getInstance());
	}

	private boolean hasAnyObjectiveSet(List<String> objectives) {
		return objectives != null && !objectives.isEmpty();
	}

	@Override
	public List<ObjectiveCalculator> createObjectives() {
		return new ArrayList<>(objectives);
	}
}
