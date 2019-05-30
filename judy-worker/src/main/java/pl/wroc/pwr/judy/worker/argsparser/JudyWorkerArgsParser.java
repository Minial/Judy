package pl.wroc.pwr.judy.worker.argsparser;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.wroc.pwr.judy.cli.ConfigException;
import pl.wroc.pwr.judy.cli.HelpException;


class JudyWorkerArgsParser {
	private static final Logger LOGGER = LogManager.getLogger(JudyWorkerArgsParser.class);
	private final String workspace;
	private final int threadsCount;

	public JudyWorkerArgsParser(final String[] args) throws ConfigException {
		final OptionSet options = getOptions(args);
		this.workspace = getWorkspace(options);
		this.threadsCount = getThreadsCount(options);
	}

	public String getWorkspace() {
		return this.workspace;
	}

	public int getThreadsCount() {
		return this.threadsCount;
	}

	/**
	 * Parse input arguments.
	 * 
	 * @param args
	 *            input arguments
	 * @return set of parsed options
	 * @throws HelpException
	 *             when <code>--help</code>, <code>-h</code> or <code>-?</code> was given as one of arguments
	 */
	private OptionSet getOptions(final String[] args) throws HelpException {
		final OptionParser parser = new OptionParser();
		setupParser(parser);
		final OptionSet options = parser.parse(args);

		if (options.has("help")) {
			try {
				final StringWriter writer = new StringWriter();
				parser.printHelpOn(writer);
				throw new HelpException(writer.toString());
			} catch (final IOException e) {
				LOGGER.error("IOException while printing help", e);
			}
		}
		return options;
	}

	/**
	 * Setup jopt parser.
	 * 
	 * @param parser
	 */
	private void setupParser(final OptionParser parser) {
		parser.acceptsAll(list("help", "h", "?"), "Print help.");
		parser.acceptsAll(list("workspace", "w"), "Set absolute path to workspace directory.").withRequiredArg()
				.describedAs("path");
		parser.acceptsAll(list("threads", "x"), "Set number of threads.").withRequiredArg().describedAs("number");
	}

	private String getWorkspace(final OptionSet options) throws ConfigException {
		if (options.has("workspace")) {
			String workspaceParam = (String) options.valueOf("workspace");
			final File workspaceFile = new File(workspaceParam);
			if (!workspaceFile.isDirectory()) {
				throw new ConfigException("Workspace is not a directory: " + workspaceParam);
			}
			final File file = new File(workspaceParam);
			try {
				workspaceParam = file.getCanonicalPath();
			} catch (final IOException e) {
				workspaceParam = file.getAbsolutePath();
			}
			return workspaceParam;
		} else {
			throw new ConfigException("Missing required argument: workspace");
		}
	}

	private int getThreadsCount(final OptionSet options) throws ConfigException {
		if (options.has("threads")) {
			final String threadsParam = (String) options.valueOf("threads");
			int count = 0;
			try {
				count = Integer.parseInt(threadsParam);
			} catch (final NumberFormatException e) {
				throw new ConfigException("Invalid threads count number: " + threadsParam);
			}
			if (count < 1) {
				throw new ConfigException("Invalid threads count number: " + threadsParam);
			}
			return count;
		} else {
			return 2;
		}
	}

	/**
	 * Create list of strings.
	 */
	private static List<String> list(final String... array) {
		return Arrays.asList(array);
	}
}
