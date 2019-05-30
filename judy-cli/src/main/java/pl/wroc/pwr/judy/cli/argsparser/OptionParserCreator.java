package pl.wroc.pwr.judy.cli.argsparser;

import joptsimple.OptionParser;

import java.util.Arrays;
import java.util.List;

public final class OptionParserCreator {

	public static final String SOURCE_CLASSES_PARAM = "classes";
	public static final String TEST_CLASSES_PARAM = "test-classes";

	private OptionParserCreator() {

	}

	/**
	 * Creates jopt parser.
	 *
	 * @return parser
	 */
	public static OptionParser createOptionParser() {

		final OptionParser parser = new OptionParser();
		parser.acceptsAll(list("help", "h", "?"), "Print help.");
		parser.acceptsAll(list("killed", "k"), "Show killed mutants in report.");
		parser.acceptsAll(list("runTestsInParallel"), "Run all tests in parallel.");

		// --cluster
		parser.acceptsAll(list("cluster", "C"), "Enable distributed computations.");

		// --som
		parser.acceptsAll(list("som", "S"), "Enable Second Order Mutants generation.").withRequiredArg()
				.describedAs("SOM algorithm");

		// --hom
		parser.acceptsAll(list("hom", "H"), "Enable High Order Mutants generation.").withRequiredArg()
				.describedAs("HOM strategy");

		// --workspace, -w
		parser.acceptsAll(list("workspace", "w"), "Set absolute path to workspace directory.").withRequiredArg()
				.describedAs("path");

		// --classes, -c
		parser.acceptsAll(list(SOURCE_CLASSES_PARAM, "c"), "Set relative path to classes directory.").withRequiredArg()
				.describedAs("path");

		// --test-classes, -t
		parser.acceptsAll(list(TEST_CLASSES_PARAM, "t"), "Set relative path to test classes directory.")
				.withRequiredArg().describedAs("path");

		// --libs, -l
		parser.acceptsAll(list("libs", "l"), "Set relative path to libs directory or to single jar file.")
				.withRequiredArg().describedAs("path");

		// --sources, -s
		parser.acceptsAll(list("sources", "s"), "Set relative path to source code directory.").withRequiredArg()
				.describedAs("path");

		// --result-path, -r
		parser.acceptsAll(list("result-path", "r"), "Set absolute path to write results.").withRequiredArg()
				.describedAs("path");

		// --properties, -p
		parser.acceptsAll(list("properties", "p"), "Set absolute path to properties file.").withRequiredArg()
				.describedAs("path");

		// --threads, -x
		parser.acceptsAll(list("threads", "x"), "Set number of threads.").withRequiredArg().describedAs("number");

		// --infiniteLoopBreakTime, -i
		parser.acceptsAll(list("maxInfiniteLoopGuardTime", "i"),
				"Set maximum waiting time for Judy's Infinite Loop Guard (in miliseconds, default 5000).")
				.withRequiredArg().describedAs("number");
		return parser;
	}

	/**
	 * Create list of strings.
	 */
	private static List<String> list(final String... array) {
		return Arrays.asList(array);
	}

}
