package pl.wroc.pwr.judy.hom.research;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moeaframework.core.Solution;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.hom.objectives.ObjectiveCalculator;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomDataCollector implements IHomDataCollector {

	private static final String SEPARATOR = "\t";
	private static final Logger LOGGER = LogManager.getLogger();
	private List<ObjectiveCalculator> objectives;
	private NumberFormat format;
	private static boolean header = false;

	/**
	 * Creates HOM data collector
	 *
	 * @param objectives HOM research objectives
	 */
	public HomDataCollector(List<ObjectiveCalculator> objectives) {
		this.objectives = objectives;
		format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setMaximumFractionDigits(3);
		format.setMinimumFractionDigits(3);
	}

	private String getHeader() {
		StringBuilder sb = new StringBuilder();
		for (ObjectiveCalculator objective : objectives) {
			sb.append(objective.getDescription()).append(SEPARATOR);
		}
		sb.append(SEPARATOR);
		sb.append("Operators").append(SEPARATOR);
		sb.append("Points").append(SEPARATOR);
		sb.append("Indexes").append(SEPARATOR);
		sb.append("Lines").append(SEPARATOR);
		sb.append("Class").append(SEPARATOR);
		sb.append("Weakly subsuming").append(SEPARATOR);
		sb.append("Strongly subsuming").append(SEPARATOR);
		sb.append("Non-intersecting").append(SEPARATOR);
		sb.append("Fault shifting").append(SEPARATOR);
		sb.append("Total fault shifting").append(SEPARATOR);
		sb.append("Bytecode array");
		return sb.toString();
	}

	@Override
	public void log(IMutant hom, List<IMutant> foms, Solution s) {
		if (!header) {
			LOGGER.info(getHeader());
			header = true;
		}
		logMutant(hom, foms, s);

	}

	private StringBuilder getObjectiveValues(Solution s) {
		StringBuilder sb = new StringBuilder();
		for (double value : s.getObjectives()) {
			sb.append(format.format(value)).append(SEPARATOR);
		}
		return sb;
	}

	private StringBuilder getHomInfo(IMutant hom) {
		StringBuilder sb = new StringBuilder();
		sb.append(hom.getOperatorsNames()).append(SEPARATOR);
		sb.append(hom.getMutionPointsIndexes()).append(SEPARATOR);
		sb.append(hom.getMutationIndexes()).append(SEPARATOR);
		sb.append(hom.getLinesNumbers()).append(SEPARATOR);
		sb.append(hom.getTargetClassName());
		return sb;
	}

	private void logMutant(IMutant hom, List<IMutant> foms, Solution solution) {
		StringBuilder content = getObjectiveValues(solution).append(SEPARATOR).append(getHomInfo(hom))
				.append(SEPARATOR).append(getSubsumingInfo(hom, foms)).append(SEPARATOR)
				.append(getShiftingInfo(hom, foms));

		if (HomTypeVerifier.isStronglySubsuming(hom, foms) || HomTypeVerifier.isTotallyShifted(hom, foms)) {
			content.append(SEPARATOR).append(Arrays.toString(hom.getBytecode().getBytecode()));
		}

		LOGGER.info(content);
	}

	private StringBuilder getShiftingInfo(IMutant hom, List<IMutant> foms) {
		StringBuilder sb = new StringBuilder();
		boolean partiallyShifted = HomTypeVerifier.isPartiallyShifted(hom, foms);
		boolean totallyShifted = false;

		if (partiallyShifted) {
			totallyShifted = HomTypeVerifier.isTotallyShifted(hom, foms);
		}
		sb.append(partiallyShifted);
		sb.append(SEPARATOR);
		sb.append(totallyShifted);
		return sb;
	}

	private StringBuilder getSubsumingInfo(IMutant hom, List<IMutant> foms) {
		StringBuilder sb = new StringBuilder();

		boolean weaklySubsuming = HomTypeVerifier.isSubsuming(hom, foms);
		boolean stronglySubsuming = HomTypeVerifier.isStronglySubsuming(hom, foms);
		boolean nonIntersecting = false;

		if (weaklySubsuming) {
			nonIntersecting = HomTypeVerifier.isNonIntersecting(hom, foms);
		}

		sb.append(weaklySubsuming);
		sb.append(SEPARATOR);
		sb.append(stronglySubsuming);
		sb.append(SEPARATOR);
		sb.append(nonIntersecting);
		return sb;
	}

}