package pl.wroc.pwr.judy.cli.argsparser;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.IMutationResultWriter;
import pl.wroc.pwr.judy.cli.ConfigException;
import pl.wroc.pwr.judy.cli.HelpException;
import pl.wroc.pwr.judy.cli.result.BaseResultFormatter;
import pl.wroc.pwr.judy.cli.result.XmlMutationResultWriter;
import pl.wroc.pwr.judy.common.IMutationOperatorsFactory;
import pl.wroc.pwr.judy.general.MutationOperatorsFactory;
import pl.wroc.pwr.judy.hom.HomStrategy;
import pl.wroc.pwr.judy.hom.objectives.IObjectivesFactory;
import pl.wroc.pwr.judy.hom.objectives.ObjectivesFactory;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;
import pl.wroc.pwr.judy.utils.BytecodeCache;
import pl.wroc.pwr.judy.utils.ClassKind;
import pl.wroc.pwr.judy.work.MutationResultFormatter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

/**
 * This class handles all of input arguments parsing. JOpt Simple library is
 * used as a base solution.
 *
 * @author pmiwaszko
 */
class JudyArgsParser {

	private static final int DEFAULT_MAX_WORK_RETRIES = 3;
	private static final int DEFAULT_HOM_MAX_EVALUATIONS = 100;
	private static final int DEFAULT_HOM_MAX_MUTATION_ORDER = 15;

	private static final String CLUSTER_PARAMETER = "cluster";
	private static final String SOM_PARAMETER = "som";

	private static final String HOM_OBJECTIVES_PROPERTY = "judy.hom.objectives";
	private static final String HOM_MAX_EVALUATIONS = "judy.hom.evaluations";
	private static final String HOM_MAX_MUTATION_ORDER = "judy.hom.maxMutationOrder";
	private static final String HOM_SKIP_SAME_LINE_MUTATIONS = "judy.hom.skipSameLineMutations";
	private static final String HOM_SKIP_TRIVIAL_MUTANT_FILTER = "judy.hom.skipTrivialMutantFilter";
	private static final String HOM_PARAMETER = "hom";

	private static final String NO_SOURCE_CLASSES_FOUND = "No source classes found";
	private static final String NO_TEST_CLASSES_FOUND = "No test classes found";
	private static final String NOT_A_RUNNABLE_J_UNIT_TEST = "Not a runnable JUnit test, removing: ";
	private static final String CLASS_DEEMED_AS_UNMUTABLE = "Class deemed as unmutable, removing: ";
	private static final String SOURCE_CLASS_REGEX = "^.*(?<!Test)$";
	private static final String TEST_CLASS_REGEX = ".*Test.*";
	private static final String DEFAULT_RESULT = "judy-result.xml";
	private static final Logger LOGGER = LogManager.getLogger(JudyArgsParser.class);
	private final boolean useCluster;
	private final boolean useSom;
	private final String workspace;
	private final List<String> classpath;
	private final List<String> sourcepath;
	private final List<String> sourceClasses;
	private final List<String> testClasses;
	private final String resultPath;
	private final String xsltPath;
	private final int maxWorkRetries;
	private final Properties properties;
	private final boolean showKilledMutants;
	private final IMutationOperatorsFactory operatorsFactory;
	private final int threadsCount;
	private final long maxInfiniteLoopGuardTimeout;
	private final int longestClassNameLength;
	private final String somAlgorithm;
	private final String arguments;
	private final boolean runTestsInParallel;

	private boolean useHom;
	private final IObjectivesFactory objectivesFactory;
	private HomStrategy homStrategy;
	private int maxHomEvaluations;
	private boolean sameLineMutations;
	private boolean skipTrivialMutantFilter;
	private int homMaxMutationOrder;

	public JudyArgsParser(final String[] args) throws ConfigException {
		final OptionSet options = parseOptions(args);
		useCluster = options.has(CLUSTER_PARAMETER);
		useSom = options.has(SOM_PARAMETER);
		useHom = options.has(HOM_PARAMETER);
		validateHomSomConfiguration();

		workspace = getWorkspace(options);

		classpath = getClasspath(options);
		sourceClasses = getSourceClasses(options);
		testClasses = getTestClasses(options);
		sourcepath = getSourcepath(options);

		longestClassNameLength = findLongestClassNameLength();
		xsltPath = "result-xslt/result-xslt.xml";
		threadsCount = getThreadsCount(options);
		maxInfiniteLoopGuardTimeout = getMaxInfiniteLoopGuardTimeout(options);
		maxWorkRetries = DEFAULT_MAX_WORK_RETRIES;
		showKilledMutants = options.has("killed");
		resultPath = getResultPath(options);
		properties = getProperties(options);
		operatorsFactory = getOperatorsFactory(options);
		arguments = concatArguments(args);
		runTestsInParallel = options.has("runTestsInParallel");

		somAlgorithm = getSomAlgorithm(options);

		homStrategy = getHomStrategy(options);
		objectivesFactory = getObjectivesFactory(options);
		maxHomEvaluations = getMaxHomEvaluations(options);
		sameLineMutations = getSameLineMutations(options);
		homMaxMutationOrder = getHomMaxMutationOrder(options);
		skipTrivialMutantFilter = getSkipTrivialMutantFilter(options);
	}

	private boolean getSkipTrivialMutantFilter(OptionSet options) {
		return parseBoolean(properties.get(HOM_SKIP_TRIVIAL_MUTANT_FILTER));
	}

	private int getHomMaxMutationOrder(OptionSet options) throws ConfigException {
		String property = properties.get(HOM_MAX_MUTATION_ORDER);
		return property == null ? DEFAULT_HOM_MAX_MUTATION_ORDER : parseMaxMutationOrder(property);
	}

	private int parseMaxMutationOrder(String property) throws ConfigException {
		int max;
		try {
			max = parseInt(property);
		} catch (NumberFormatException e) {
			throw new ConfigException("Invalid property value: " + HOM_MAX_MUTATION_ORDER);
		}

		if (max < 2) {
			throw new ConfigException("Invalid property value: " + HOM_MAX_MUTATION_ORDER + "- must be greater than 2");
		}
		return max;
	}

	private boolean getSameLineMutations(OptionSet options) {
		return parseBoolean(properties.get(HOM_SKIP_SAME_LINE_MUTATIONS));
	}

	private void validateHomSomConfiguration() throws ConfigException {
		if (useSom && useHom) {
			throw new ConfigException(format("Cannot use Judy for SOM and HOM generation at the same time. Use only one of the params: -%s or -%s", SOM_PARAMETER, HOM_PARAMETER));
		}
	}

	private int findLongestClassNameLength() {
		final String longestClassName = Collections.max(sourceClasses, new Comparator<String>() {

			@Override
			public int compare(final String o1, final String o2) {
				return o1.length() - o2.length();
			}

		});
		return longestClassName.length();
	}

	public boolean getUseCluster() {
		return useCluster;
	}

	public String getArguments() {
		return arguments;
	}

	public String getWorkspace() {
		return workspace;
	}

	public List<String> getClasspath() {
		return classpath;
	}

	public List<String> getTestClasses() {
		return testClasses;
	}

	public List<String> getSourceClasses() {
		return sourceClasses;
	}

	public IMutationOperatorsFactory getOperatorsFactory() {
		return operatorsFactory;
	}

	public int getMaxWorkRetries() {
		return maxWorkRetries;
	}

	public boolean getShowKilledMutants() {
		return showKilledMutants;
	}

	public IMutationResultWriter getResultWriter() {
		return new XmlMutationResultWriter(resultPath, xsltPath, workspace, sourcepath);
	}

	public int getThreadsCount() {
		return threadsCount;
	}

	public final String getSomAlgorithm() {
		return somAlgorithm;
	}

	private HomStrategy getHomStrategy(OptionSet options) throws ConfigException {
		String parameter = (String) options.valueOf(HOM_PARAMETER);
		HomStrategy strategy = HomStrategy.lookup(parameter);

		if (useHom) {
			if (strategy == null) {
				throw new ConfigException("Missing required argument: HOM strategy, see --help.");
			} else {
				LOGGER.info("Will use " + parameter + " HOM generation strategy.");
			}
		}
		return strategy;
	}

	private boolean isEmpty(String parameter) {
		return parameter == null || "".equals(parameter);
	}

	private String getSomAlgorithm(final OptionSet options) throws ConfigException {
		String algorithm = (String) options.valueOf(SOM_PARAMETER);
		if (isEmpty(algorithm) && useSom) {
			throw new ConfigException("Missing required argument: SOM algorithm, see --help.");
		}
		return algorithm;
	}

	public boolean getUseSom() {
		return useSom;
	}

	/**
	 * @return the longestClassNameLength
	 */
	public int getLongestClassNameLength() {
		return longestClassNameLength;
	}

	/**
	 * @return the useHom
	 */
	public boolean getUseHom() {
		return useHom;
	}

	/**
	 * Parse input arguments.
	 *
	 * @param args input arguments
	 * @return set of parsed options
	 * @throws HelpException when <code>--help</code>, <code>-h</code> or <code>-?</code>
	 *                       was given as one of arguments
	 */
	private OptionSet parseOptions(final String[] args) throws HelpException {
		final OptionParser parser = OptionParserCreator.createOptionParser();
		final OptionSet options = parser.parse(args);

		if (options.has("help")) {
			try {
				final StringWriter writer = new StringWriter();
				parser.printHelpOn(writer);
				throw new HelpException(writer.toString());
			} catch (final IOException e) {
				// no help message for you then
			}
		}
		return options;
	}

	private String concatArguments(final String[] args) {
		final StringBuilder concatArgs = new StringBuilder();
		for (final String arg : args) {
			concatArgs.append(arg);
			concatArgs.append(" ");
		}
		return concatArgs.toString();
	}

	private String getWorkspace(final OptionSet options) throws ConfigException {
		if (options.has("workspace")) {
			String workspaceParam = (String) options.valueOf("workspace");
			final File workspaceFile = new File(workspaceParam);
			if (!workspaceFile.isDirectory()) {
				throw new ConfigException("Workspace is not directory: " + workspaceParam);
			}
			final File file = new File(workspaceParam);
			try {
				workspaceParam = file.getCanonicalPath();
			} catch (final IOException e) {
				workspaceParam = file.getAbsolutePath();
			}
			return workspaceParam;
		} else {
			throw new ConfigException("Missing required argument: workspace, see --help.");
		}
	}

	private List<String> getSourceClasses(final OptionSet options) throws ConfigException {
		return getClasses(options.valuesOf(OptionParserCreator.SOURCE_CLASSES_PARAM), SOURCE_CLASS_REGEX,
				CLASS_DEEMED_AS_UNMUTABLE, NO_SOURCE_CLASSES_FOUND);
	}

	private List<String> getTestClasses(final OptionSet options) throws ConfigException {
		return getClasses(options.valuesOf(OptionParserCreator.TEST_CLASSES_PARAM), TEST_CLASS_REGEX,
				NOT_A_RUNNABLE_J_UNIT_TEST, NO_TEST_CLASSES_FOUND);
	}

	/**
	 * Extracts mutable classes from class names list
	 *
	 * @param classes list of class names
	 * @param info    debug info text to be shown if class in not mutable
	 * @return list of mutable classes
	 */
	private List<String> extractPotentiallyMutableClasses(final List<String> classes, final String info) {
		final List<String> mutableClasses = new LinkedList<>();
		final BytecodeCache cache = new BytecodeCache(workspace, classpath);
		for (final String className : classes) {
			if (ClassKind.isPotentiallyMutable(className, cache.get(className))) {
				mutableClasses.add(className);
			} else {
				LOGGER.info(info + className);
			}
		}
		return mutableClasses;
	}

	/**
	 * Gets only class names fulfilling regex
	 *
	 * @param options list of paths set via CLI
	 * @param regex   rule for class name
	 * @return list of class names
	 */
	private List<String> filterClasses(final List<?> options, final String regex) {
		final List<String> classes = new LinkedList<>();
		for (final Object option : options) {
			final Directory directory = new Directory(workspace, (String) option, regex);
			classes.addAll(directory.listClasses());
		}
		return classes;
	}

	/**
	 * Gets mutable class names
	 *
	 * @param options      list of paths set via CLI
	 * @param regex        rule for class name
	 * @param removeInfo   debug info text to be shown if class is not mutable
	 * @param notFoundInfo debug info text to be shown if classes were not found
	 * @return list of mutable classes fulfilling class name rule
	 * @throws ConfigException if not mutable or runnable classes were found
	 */
	private List<String> getClasses(final List<?> options, final String regex, final String removeInfo,
									final String notFoundInfo) throws ConfigException {
		final List<String> filteredClasses = filterClasses(options, regex);
		final List<String> mutableClasses = extractPotentiallyMutableClasses(filteredClasses, removeInfo);

		if (mutableClasses.isEmpty()) {
			throw new ConfigException(notFoundInfo);
		}
		return mutableClasses;
	}

	private List<String> getClasspath(final OptionSet options) throws ConfigException {
		final List<String> resultClasspath = new LinkedList<>();
		List<?> set = options.valuesOf(OptionParserCreator.SOURCE_CLASSES_PARAM);
		for (final Object option : set) {
			final String description = (String) option;
			// take only first part from description (which is path), see
			// Directory class
			resultClasspath.add(description.split(";")[0]);
		}
		set = options.valuesOf(OptionParserCreator.TEST_CLASSES_PARAM);
		for (final Object option : set) {
			final String description = (String) option;
			// take only first part from description (which is path), see
			// Directory class
			resultClasspath.add(description.split(";")[0]);
		}
		set = options.valuesOf("libs");
		for (final Object option : set) {
			final String lib = (String) option;
			final File file = new File(workspace, lib);
			if (!file.exists()) {
				throw new ConfigException("Path with libraries does not exist: " + file);
			}
			if (lib.toLowerCase().endsWith(".jar")) {
				resultClasspath.add(lib);
			} else {
				final Directory directory = new Directory(workspace, lib);
				resultClasspath.addAll(directory.listJars());
			}
		}
		return resultClasspath;
	}

	private List<String> getSourcepath(final OptionSet options) {
		final List<String> resultSourcePath = new LinkedList<>();
		final List<?> set = options.valuesOf("sources");
		for (final Object option : set) {
			resultSourcePath.add((String) option);
		}
		return resultSourcePath;
	}

	private String getResultPath(final OptionSet options) throws ConfigException {
		if (options.has("result-path")) {
			String path = (String) options.valueOf("result-path");
			final File result = new File(path);
			// if given path denotes a directory append default name to it
			if (result.isDirectory()) {
				path = new File(result, DEFAULT_RESULT).getAbsolutePath();
			} else if (result.exists() && !result.canWrite()) {
				throw new ConfigException("Report file already exists and cannot be written: " + path);
			}
			return path;
		} else {
			return DEFAULT_RESULT;
		}
	}

	private Properties getProperties(final OptionSet options) throws ConfigException {
		final Properties resultProperties = new Properties();
		if (options.has("properties")) {
			final String path = (String) options.valueOf("properties");
			try {
				resultProperties.load(path);
			} catch (final IOException e) {
				throw new ConfigException("Unable to open specified properties file: " + path);
			}
		}
		return resultProperties;
	}

	private IMutationOperatorsFactory getOperatorsFactory(final OptionSet options) {
		final List<String> operators = properties.getList("judy.operators");
		if (operators != null && !operators.isEmpty()) {
			LOGGER.info("Will add these operators: " + operators.toString());
		} else {
			LOGGER.info("No judy.operators property found, will add all...");
		}
		return new MutationOperatorsFactory(operators);
	}

	private int getThreadsCount(final OptionSet options) throws ConfigException {
		if (options.has("threads")) {
			final String numberOfThreads = (String) options.valueOf("threads");
			int count;
			try {
				count = parseInt(numberOfThreads);
			} catch (final NumberFormatException e) {
				throw new ConfigException("Invalid threads count number: " + numberOfThreads);
			}

			if (count < 1) {
				throw new ConfigException("Invalid threads count number: " + numberOfThreads);
			}
			return count;
		} else {
			return 2;
		}
	}

	private long getMaxInfiniteLoopGuardTimeout(final OptionSet options) throws ConfigException {
		if (options.has("maxInfiniteLoopGuardTimeout")) {
			final String infiniteLoopBreakingTime = (String) options.valueOf("maxInfiniteLoopGuardTimeout");
			long maxInfiniteLoopGuardTimeout = 0;
			try {
				maxInfiniteLoopGuardTimeout = Long.parseLong(infiniteLoopBreakingTime);
			} catch (final NumberFormatException e) {
				throw new ConfigException("Invalid time value passed to infinite loop guard: "
						+ maxInfiniteLoopGuardTimeout);
			}
			if (maxInfiniteLoopGuardTimeout < 0) {
				throw new ConfigException("Invalid time value passed to infinite loop guard: "
						+ maxInfiniteLoopGuardTimeout);
			}
			return maxInfiniteLoopGuardTimeout;
		} else {
			return InfiniteLoopGuard.DEFAULT_TIMEOUT;
		}
	}

	private IObjectivesFactory getObjectivesFactory(final OptionSet options) {
		final List<String> objectives = properties.getList(HOM_OBJECTIVES_PROPERTY);
		return useHom ? new ObjectivesFactory(objectives) : null;
	}

	private int getMaxHomEvaluations(OptionSet options) throws ConfigException {
		String property = properties.get(HOM_MAX_EVALUATIONS);
		if (property != null) {
			LOGGER.info("HOM searching maximum evaluations: " + property);
			return parseMaxEvaluations(property);
		} else {
			return DEFAULT_HOM_MAX_EVALUATIONS;
		}
	}

	private int parseMaxEvaluations(String property) throws ConfigException {
		try {
			return parseInt(property);
		} catch (NumberFormatException e) {
			throw new ConfigException("Invalid property value: judy.hom.evaluations");
		}
	}

	public IObjectivesFactory getObjectivesFactory() {
		return objectivesFactory;
	}

	public MutationResultFormatter getResultFormatter() {
		return new BaseResultFormatter(longestClassNameLength);
	}

	public HomStrategy getHomStrategy() {
		return homStrategy;
	}

	public int getMaxHomEvaluations() {
		return maxHomEvaluations;
	}

	/**
	 * @return true is skipping same line mutations was set, default: false
	 */
	public boolean isSkippingSameLineMutations() {
		return sameLineMutations;
	}

	public int getMaxMutationOrder() {
		return homMaxMutationOrder;
	}

	public boolean isRunTestsInParallel() {
		return runTestsInParallel;
	}

	public long getMaxInfiniteLoopGuardTimeout() {
		return maxInfiniteLoopGuardTimeout;
	}

	public boolean shouldSkipTrivialMutantFilter() {
		return skipTrivialMutantFilter;
	}

}
