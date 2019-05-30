package pl.wroc.pwr.judy.cli.result;

import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.utils.ClassKind;
import pl.wroc.pwr.judy.work.MutationSummary;

public class BaseResultFormatter extends AbstractMutationResultFormatter {

	private static final String HEADER_DURATION = "Tests duration [ms]";
	private static final String HEADER_TEST_METHODS = "Test methods";
	private static final String HEADER_ALL = "All";
	private static final String HEADER_KILLED = "Killed";
	private static final String HEADER_TEST_CLASSES = "Test classes";
	private static final String HEADER_CLASS = "Class";
	private static final String HEADER_CLASS_KIND = "ClassKind";
	private static final String HEADER_PROGRESS = "Progress";

	private final int maxClassNameLength;

	/**
	 * Creates formatter with maxClassNameLength
	 *
	 * @param maxClassNameLength longest class name length
	 */
	public BaseResultFormatter(final int maxClassNameLength) {
		this.maxClassNameLength = maxClassNameLength;
	}

	@Override
	public String getSummary(final MutationSummary mutationSummary, final ITargetClass targetClass, final ClassKind kind, final IDurationStatistic statistic) {
		return SPACER + addKind(kind) + addClassName(targetClass) + addKilledCount(mutationSummary)
				+ addAllCount(mutationSummary) + addCoveringTestsCount(targetClass) + addTestMethodsCount(statistic)
				+ addTestsDuration(statistic);
	}

	private String addTestsDuration(final IDurationStatistic statistic) {
		return padRight(statistic.getTestsDuration(), HEADER_DURATION.length());
	}

	private String addTestMethodsCount(final IDurationStatistic statistic) {
		return padRight(statistic.getTestMethods(), HEADER_TEST_METHODS.length()) + SPACER;
	}

	private String addAllCount(final MutationSummary mutationSummary) {
		return padRight(mutationSummary.getAll(), DEFAULT_PADDING) + SPACER;
	}

	private String addKilledCount(final MutationSummary mutationSummary) {
		return padRight(mutationSummary.getKilled(), HEADER_KILLED.length()) + SPACER;
	}

	private String addCoveringTestsCount(final ITargetClass targetClass) {
		return padRight(targetClass.getCoveringTestClassesCount(), HEADER_TEST_CLASSES.length()) + SPACER;
	}

	private String addClassName(final ITargetClass targetClass) {
		return pad(targetClass.getName(), maxClassNameLength) + SPACER;
	}

	private String addKind(final ClassKind kind) {
		return pad(kind, HEADER_CLASS_KIND.length()) + SPACER;
	}

	@Override
	public String getHeader() {
		final StringBuilder sb = new StringBuilder();

		sb.append(HEADER_PROGRESS);
		sb.append(SPACER);
		sb.append(HEADER_CLASS_KIND);
		sb.append(SPACER);
		sb.append(pad(HEADER_CLASS, maxClassNameLength));
		sb.append(SPACER);
		sb.append(HEADER_KILLED);
		sb.append(SPACER);
		sb.append(padRight(HEADER_ALL, DEFAULT_PADDING));
		sb.append(SPACER);
		sb.append(HEADER_TEST_CLASSES);
		sb.append(SPACER);
		sb.append(HEADER_TEST_METHODS);
		sb.append(SPACER);
		sb.append(HEADER_DURATION);
		return sb.toString();
	}

}
