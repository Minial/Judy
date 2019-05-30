package pl.wroc.pwr.judy.hom.research;

import org.moeaframework.core.Solution;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;

import java.util.List;

public final class HomQualityVerifier {

	private HomQualityVerifier() {
	}

	/**
	 * Verifies if given solution is of high quality
	 *
	 * @param objectives mutation objectives
	 * @param solution   MOEA framework solution
	 * @return true if all objectives are indicating high quality
	 */
	public static boolean verify(List<ObjectiveCalculator> objectives, Solution solution) {
		boolean result = !objectives.isEmpty() && haveEqualSize(objectives, solution);
		for (int i = 0; i < objectives.size(); i++) {
			result = result && objectives.get(i).isValuable(solution.getObjective(i));
		}
		return result;
	}

	private static boolean haveEqualSize(List<ObjectiveCalculator> objectives, Solution solution) {
		return objectives.size() == solution.getObjectives().length;
	}

}
