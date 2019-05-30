package pl.wroc.pwr.judy.hom.objectives;

import java.util.List;

/**
 * HOM objectives factory for mutant evaluation
 *
 * @author TM
 */
public interface IObjectivesFactory {
	/**
	 * Creates objectives set in configuration file
	 *
	 * @return objective calculators
	 */
	List<ObjectiveCalculator> createObjectives();
}
